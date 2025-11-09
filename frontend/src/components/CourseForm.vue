<script>
import axios from 'axios'

export default {
  name: 'CourseForm',

  data() {
    return {
      course: {
        acronym: '',
        title: '',
        ects: '',
        block: '',
        professor: ''
      },
      successMessage: '',
      errorMessage: '',
      validationErrors: {}
    }
  },

  emits: ['course-added'],

  methods: {
    async submitForm() {
      this.successMessage = ''
      this.errorMessage = ''
      this.validationErrors = {}

      try {
        const response = await axios.post('http://localhost:8080/api/courses', this.course)
        this.successMessage = `Le cours ${response.data.acronym} a été ajouté avec succès !`
        this.$emit('course-added', response.data)
        this.course = { acronym: '', title: '', ects: '', block: '', professor: '' }
      } catch (error) {
        if (error.response && error.response.status === 400) {
          this.validationErrors = error.response.data
        } else {
          this.errorMessage = "Une erreur est survenue lors de l'ajout du cours."
        }
      }
    }
  }
}
</script>

<template>
  <div class="max-w-md mx-auto bg-black/40 rounded-xl p-6 space-y-4 border border-gray-800">
    <h2 class="text-2xl font-semibold text-emerald-400 text-center mb-4">
      Ajouter un cours
    </h2>

    <form @submit.prevent="submitForm" class="space-y-3">
      <div>
        <label class="block text-sm font-medium text-gray-300">Acronyme</label>
        <input
          v-model="course.acronym"
          type="text"
          class="bg-gray-900/50 text-gray-100 border border-gray-700 focus:border-emerald-500 focus:ring focus:ring-emerald-700/40 p-2 w-full rounded-md transition"
        />
        <p v-if="validationErrors.acronym" class="text-red-500 text-sm mt-1">
          {{ validationErrors.acronym }}
        </p>
      </div>

      <div>
        <label class="block text-sm font-medium text-gray-300">Titre</label>
        <input
          v-model="course.title"
          type="text"
          class="bg-gray-900/50 text-gray-100 border border-gray-700 focus:border-emerald-500 focus:ring focus:ring-emerald-700/40 p-2 w-full rounded-md transition"
        />
        <p v-if="validationErrors.title" class="text-red-500 text-sm mt-1">
          {{ validationErrors.title }}
        </p>
      </div>

      <div class="grid grid-cols-2 gap-3">
        <div>
          <label class="block text-sm font-medium text-gray-300">ECTS</label>
          <input
            v-model.number="course.ects"
            type="number"
            class="bg-gray-900/50 text-gray-100 border border-gray-700 focus:border-emerald-500 focus:ring focus:ring-emerald-700/40 p-2 w-full rounded-md transition"
          />
          <p v-if="validationErrors.ects" class="text-red-500 text-sm mt-1">
            {{ validationErrors.ects }}
          </p>
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-300">Bloc</label>
          <input
            v-model.number="course.block"
            type="number"
            class="bg-gray-900/50 text-gray-100 border border-gray-700 focus:border-emerald-500 focus:ring focus:ring-emerald-700/40 p-2 w-full rounded-md transition"
          />
          <p v-if="validationErrors.block" class="text-red-500 text-sm mt-1">
            {{ validationErrors.block }}
          </p>
        </div>
      </div>

      <div>
        <label class="block text-sm font-medium text-gray-300">Professeur</label>
        <input
          v-model="course.professor"
          type="text"
          class="bg-gray-900/50 text-gray-100 border border-gray-700 focus:border-emerald-500 focus:ring focus:ring-emerald-700/40 p-2 w-full rounded-md transition"
        />
      </div>

      <button
        type="submit"
        class="bg-emerald-600 hover:bg-emerald-500 text-white font-semibold py-2 px-4 rounded-md w-full shadow-md shadow-emerald-900/40 transition"
      >
        Ajouter
      </button>
    </form>

    <p v-if="successMessage" class="text-emerald-400 text-center font-medium mt-4">
      {{ successMessage }}
    </p>
    <p v-if="errorMessage" class="text-red-500 text-center font-medium mt-4">
      {{ errorMessage }}
    </p>
  </div>
</template>
