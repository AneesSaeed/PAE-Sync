<script>
import axios from 'axios'
import GenericTable from '@/components/GenericTable.vue'
import CourseForm from '@/components/CourseForm.vue'
import BaseModal from '@/components/BaseModal.vue'
import PrerequisiteForm from '@/components/PrerequisiteForm.vue'
import CourseDetails from '@/components/CourseDetails.vue'
import SearchBar from '@/components/SearchBar.vue'



export default {
  name: 'HomeView',
  components: { GenericTable, CourseForm, BaseModal, PrerequisiteForm, CourseDetails, SearchBar },

  data() {
    return {
      allCourses: [],  // ← full list from backend
      courses: [],     // ← what’s currently displayed

      loading: true,
      error: null,

      searchTerm: '',    
      searchTimeout: null,

      showCourseModal: false,
      showPrereqModal: false,
      showDetailsModal: false,
      
      selectedCourse: null,

      headers: ['Acronym', 'Title', 'ECTS', 'Block'],
      columns: ['acronym', 'title', 'ects', 'block'],
    }
  },

  mounted() {
    this.fetchCourses()
  },

  methods: {
    async fetchCourses() {
      try {
        const response = await axios.get('http://localhost:8080/api/courses')
        this.allCourses = response.data
        this.courses = [...this.allCourses]
      } catch (err) {
        console.error(err)
        this.error = 'Impossible de charger les cours.'
      } finally {
        this.loading = false
      }
    },

    async refreshCourses() {
      await this.fetchCourses()
      this.showCourseModal = false
    },

    refreshAfterPrereq() {
      this.showPrereqModal = false
    },

    async deleteCourse(course) {
      try {
        await axios.delete(`http://localhost:8080/api/courses/${course.acronym}`)
        this.courses = this.courses.filter(c => c.acronym !== course.acronym)
        console.log(`Cours ${course.acronym} supprimé.`)
      } catch (err) {
        console.error(err)
        alert('Erreur lors de la suppression du cours.')
      }
    },

    openDetails(course) {
      this.selectedCourse = course
      this.showDetailsModal = true
    },
  },

  watch: {
    searchTerm: {
      handler(newTerm) {
        clearTimeout(this.searchTimeout);
        this.searchTimeout = setTimeout(async () => {
          const term = newTerm.trim();

          if (term === '') {
            this.courses = [...this.allCourses];
            return;
          }

          try {
            const response = await axios.get(`http://localhost:8080/api/courses/search?q=${term}`);
            const matchingAcronyms = response.data.map(c => c.acronym);

            this.courses = this.allCourses.filter(course =>
              matchingAcronyms.includes(course.acronym)
            );
          } catch (err) {
            console.error(err);
            this.error = "Erreur lors de la recherche des cours.";
          }
        }, 400);
      },
      immediate: false,
    },
  },

}
</script>

<template>
  <main
  class="min-h-screen p-8 text-gray-100 bg-gray-950 relative overflow-hidden"
  style="background-image: url('/images/double-bubble-dark.webp');  
         background-size: 256px 256px;
         background-repeat: repeat;
         background-position: center;"
  >
    <!-- Header -->
    <h1 class="text-4xl font-semibold text-center text-emerald-400 mb-8">
      Liste des cours
    </h1>


    <SearchBar 
      v-model="searchTerm" 
    />


    <!-- Loading / Error -->
    <div v-if="loading" class="text-center text-gray-600 text-lg">
      Chargement des données...
    </div>
    <div v-else-if="error" class="text-center text-red-600 text-lg">
      {{ error }}
    </div>

    <!-- Main Content -->
    <div v-else class="flex flex-col justify-center">
      <div class="flex justify-center">
        <div class="w-[65%]">
            <div class="flex justify-between mb-6">
              <button
                @click="showCourseModal = true"
                class="border border-emerald-500/70 text-white font-semibold py-2 px-6 rounded-xl 
                      bg-transparent hover:bg-emerald-600/10 active:scale-95 
                      shadow-[0_0_8px_1px_rgba(16,185,129,0.3)] "
              >
                + Ajouter un cours
              </button>

              <button
                @click="showPrereqModal = true"
                :disabled="courses.length < 2"
                class="border border-emerald-500/70 text-white font-semibold py-2 px-6 rounded-xl 
                      bg-transparent hover:bg-emerald-600/10 active:scale-95 
                      shadow-[0_0_8px_1px_rgba(16,185,129,0.3)] 
                      disabled:bg-gray-500/80 disabled:text-gray-400 disabled:cursor-not-allowed"
                      
                :title="courses.length < 2 ? 'Add at least two courses inorder to define a prerequisite' : ''"
                :aria-disabled="courses.length < 2"
                aria-label="Add a prerequisite"
              >
                + Ajouter un prérequis
              </button>
            </div>
            <GenericTable
              :headers="headers"
              :columns="columns"
              :rows="courses"
              @delete-row="deleteCourse"  
              @row-clicked="openDetails"
            />
        </div>
      </div>


      <!-- add Course Modal -->
      <BaseModal v-model="showCourseModal">
          <CourseForm @course-added="refreshCourses" />
      </BaseModal>

      <!-- add prerequisite Modal -->
      <BaseModal v-model="showPrereqModal">
          <PrerequisiteForm @prerequisite-added="refreshAfterPrereq" />
      </BaseModal>

      <!-- course detail modal -->
      <BaseModal v-model="showDetailsModal">
      <CourseDetails v-if="selectedCourse" :course="selectedCourse" />
    </BaseModal>
    </div>
  </main>
</template>
