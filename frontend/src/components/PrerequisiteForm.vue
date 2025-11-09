<script>
import axios from 'axios'

export default {
  name: 'PrerequisiteForm',
  data() {
    return {
      courses: [],
      fromCourse: '',
      toCourse: '',
      loading: true,
      successMessage: '',
      errorMessage: ''
    }
  },
  emits: ['prerequisite-added'],

  async mounted() {
    try {
      const response = await axios.get('http://localhost:8080/api/prerequisites/nodes')
      this.courses = response.data
    } catch (error) {
      this.errorMessage = "Impossible de charger les cours depuis Neo4j."
      console.error(error)
    } finally {
      this.loading = false
    }
  },

  computed: {
    availableToCourses() {
      // Filter out the selected “from” course
      return this.courses.filter(c => c !== this.fromCourse)
    }
  },

  methods: {
    async submitForm() {
      this.successMessage = ''
      this.errorMessage = ''

      if (!this.fromCourse || !this.toCourse) {
        this.errorMessage = "Veuillez sélectionner deux cours."
        return
      }

      try {
        const url = `http://localhost:8080/api/prerequisites?from=${this.fromCourse}&to=${this.toCourse}`
        const response = await axios.post(url)
        this.successMessage = response.data.message

        // reset selections
        this.fromCourse = ''
        this.toCourse = ''

        // fade success message
        setTimeout(() => {
            this.successMessage = ''
            this.$emit('prerequisite-added')
        }, 2500)
      } catch (error) {
        if (error.response?.data?.error) {
          this.errorMessage = error.response.data.error
        } else {
          this.errorMessage = "Erreur lors de l’ajout du prérequis."
        }
      }
    }
  }
}
</script>

<template>
  <div class="max-w-xl mx-auto bg-black/40 rounded-xl border border-gray-800 p-10">
    <h2 class="text-3xl font-semibold text-emerald-400 text-center mb-4">
      Ajouter un prérequis
    </h2>

    <!-- Loading -->
    <div v-if="loading" class="text-center text-gray-400 italic">
      Chargement des cours...
    </div>

    <!-- If fewer than 2 courses exist -->
    <div v-else-if="courses.length < 2" class="text-center text-gray-400 italic">
      Il faut au moins deux cours pour définir un prérequis.
    </div>

    <!-- Form -->
    <form v-else @submit.prevent="submitForm" class="space-y-6">
      <div>
        <label class="block text-base font-medium text-gray-300">Cours prérequis</label>
        <select 
          v-model="fromCourse" 
          class="bg-gray-900/50 text-white border border-gray-700 focus:border-emerald-500 focus:ring focus:ring-emerald-700/40 p-3 w-full rounded-md"
        >
          <option value="" disabled>-- Sélectionnez un cours --</option>
            <option 
              v-for="course in courses" 
              :key="course" 
              :value="course" 
              class="bg-gray-900 text-white hover:bg-emerald-700"
            >
            {{ course }}
          </option>
        </select>
      </div>

      <div>
        <label class="block text-base font-medium text-gray-300">Cours dépendant</label>
        <select 
          v-model="toCourse" 
          :disabled="!fromCourse"
          class="bg-gray-900/50 text-white border border-gray-700 focus:border-emerald-500 focus:ring focus:ring-emerald-700/40 p-3 w-full rounded-md transition disabled:opacity-40"
        >
          <option value="" disabled>-- Sélectionnez un cours --</option>
          <option 
            v-for="course in availableToCourses" 
            :key="course + '-dep'" 
            :value="course" 
            class="bg-gray-900 text-white hover:bg-emerald-700"
          >
            {{ course }}
          </option>
        </select>
      </div>

      <button
        type="submit"
        class="bg-emerald-600 hover:bg-emerald-500 text-white font-semibold py-2 px-4 rounded-md w-full shadow-md shadow-emerald-900/40 transition"
      >
        Ajouter le prérequis
      </button>
    </form>

    <p v-if="successMessage" class="text-emerald-400 text-center font-medium mt-2">
      {{ successMessage }}
    </p>
    <p v-if="errorMessage" class="text-red-500 text-center font-medium mt-2">
      {{ errorMessage }}
    </p>
  </div>
</template>
