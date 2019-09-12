package com.codegod.sarafan.service;

import com.codegod.sarafan.domain.Message;
import com.codegod.sarafan.domain.User;
import com.codegod.sarafan.domain.UserSubscription;
import com.codegod.sarafan.domain.Views;
import com.codegod.sarafan.dto.EventType;
import com.codegod.sarafan.dto.MessagePageDto;
import com.codegod.sarafan.dto.MetaDto;
import com.codegod.sarafan.dto.ObjectType;
import com.codegod.sarafan.repo.MessageRepo;
import com.codegod.sarafan.repo.UserSubscriptionRepo;
import com.codegod.sarafan.util.WsSender;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author XE on 07.09.2019
 * @project sarafan
 */

@Service
public class MessageService {

    private static String URL_PATTERN = "https?:\\/\\/?[\\w\\d\\._\\-%\\/\\?=&#]+";
    private static String IMAGE_PATTERN = "\\.(jpeg|jpg|gif|png)$";

    private static Pattern URL_REGEX = Pattern.compile(URL_PATTERN, Pattern.CASE_INSENSITIVE);
    private static Pattern IMG_REGEX = Pattern.compile(IMAGE_PATTERN, Pattern.CASE_INSENSITIVE);


    private final MessageRepo messageRepo;
    private final UserSubscriptionRepo userSubscriptionRepo;
    private final BiConsumer<EventType, Message> wsSender;

    @Autowired
    public MessageService(MessageRepo messageRepo, UserSubscriptionRepo userSubscriptionRepo, WsSender wsSender) {
        this.messageRepo = messageRepo;
        this.userSubscriptionRepo = userSubscriptionRepo;
        this.wsSender = wsSender.getSender(ObjectType.MESSAGE, Views.FullMessage.class);
    }

    private void fillMethod(Message message) throws IOException {
        String text = message.getText();
        Matcher matcher = URL_REGEX.matcher(text);
        if (matcher.find()){
            String url = text.substring(matcher.start(), matcher.end());
            matcher = IMG_REGEX.matcher(url);
            message.setLink(url);
            if (matcher.find()){
                message.setLinkCover(url);
            } else if (!url.contains("youtu")){
                MetaDto metaDto = getMeta(url);
                message.setLinkCover(metaDto.getCover());
                message.setLinkTitle(metaDto.getTitle());
                message.setLinkDescription(metaDto.getDescription());
            }
        }
    }

    private MetaDto getMeta(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements title = doc.select("meta[name$=title],meta[property$=title]");
        Elements description = doc.select("meta[name$=description],meta[property$=description]");
        Elements cover = doc.select("meta[name$=image],meta[property$=image]");
        return new MetaDto(
                getContent(title.first()),
                getContent(description.first()),
                getContent(cover.first())

        );
    }

    private String getContent(Element element) {
        return element == null ? "" : element.attr("content");
    }

    public void delete(Message message) {
        messageRepo.delete(message);
        wsSender.accept(EventType.REMOVE, message);
    }

    public Message update(Message message, Message messageFromDb) throws IOException {
        messageFromDb.setText(message.getText());
        fillMethod(messageFromDb);
        Message updatedMessage = messageRepo.save(messageFromDb);
        wsSender.accept(EventType.UPDATE, updatedMessage);
        return updatedMessage;
    }

    public Message create(Message message, User user) throws IOException {
        message.setCreationDate(LocalDateTime.now());
        fillMethod(message);
        message.setAuthor(user);
        Message updatedMessage = messageRepo.save(message);
        wsSender.accept(EventType.CREATE, updatedMessage);
        return updatedMessage;
    }

    public MessagePageDto findForUser(Pageable pageable, User user) {
        List<User> channels = userSubscriptionRepo.findBySubscriber(user)
                .stream()
                .filter(UserSubscription::isActive)
                .map(UserSubscription::getChannel)
                .collect(Collectors.toList());

        channels.add(user);

        Page<Message> page = messageRepo.findByAuthorIn(channels, pageable);

        return new MessagePageDto(
                page.getContent(),
                pageable.getPageNumber(),
                page.getTotalPages()
        );
    }

    public MessagePageDto findAllMessages() {
        List<Message> all = messageRepo.findAll();
        return new MessagePageDto(all,0, 3);
    }
}
