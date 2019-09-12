<template>
    <iframe
            width="560"
            height="315"
            :src="targetSrc"
            frameborder="0"
            allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
            allowfullscreen></iframe>
</template>

<script>
    export default {
        name: 'YouTube',
        props: ['src'],
        computed: {
            targetSrc() {
                let parts = this.src.split('/');
                let part = parts[parts.length - 1].substring(8,19);
                return 'https://www.youtube.com/embed/' + part
            }
        }
    }
</script>

<style scoped>

</style>