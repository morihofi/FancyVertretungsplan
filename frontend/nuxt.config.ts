// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: '2024-04-03',
  devtools: { enabled: true },
  modules: ["@nuxtjs/tailwindcss"],
  css: [
    "~/assets/css/main.css",
  ],
  app: {
    head: {
      charset: "utf-8",
      viewport: "width=device-width, initial-scale=1",
      title: "Vertretungsplan",
      titleTemplate: '%s - Vertretungsplan',
    },
  },
  runtimeConfig: {
		public: {
			API_URL: '' // defined in .env
		}
	},

})