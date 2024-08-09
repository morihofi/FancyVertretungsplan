<script setup lang="ts">
useHead({
  meta: [{
    property: 'og:title', content: `${useRoute().meta.title} - Vertretungsplan`
  }],
});

</script>
<template>
  <div class="min-h-screen background-main">
    <FancyNavbar />
    <div class="min-h-screen">
      <!-- Wrapper div start-->
      <div class="container mx-auto px-4 py-8 text-white">
        <div class="pt-10">
          <slot />
        </div>
      </div>
    </div>
    <FancyFooter />
  </div>
</template>