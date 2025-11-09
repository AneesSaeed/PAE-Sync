<script>
import axios from 'axios'
import IconTrash from '@/components/icons/IconTrash.vue'
import { Network } from 'vis-network/standalone'
import 'vis-network/styles/vis-network.css'

export default {
  name: 'CourseDetails',
  components: { IconTrash },
  props: {
    course: { type: Object, required: true }
  },
  data() {
    return {
      prerequisites: [],
      loading: true,
      error: null,
      successMessage: '',
      network: null
    }
  },

  async mounted() {
    await this.fetchPrerequisites()
  },

  methods: {
    async fetchPrerequisites() {
      this.loading = true
      this.error = null

      try {
        const { data } = await axios.get(
          `http://localhost:8080/api/prerequisites/${this.course.acronym}`
        )

        this.prerequisites = data || []
        this.$nextTick(this.renderGraph)
      } catch (err) {
        console.error(err)
        this.error = 'Impossible de charger les prérequis.'
      } finally {
        this.loading = false
      }
    },

    async removePrerequisite(preAcronym) {
      const confirmed = confirm(
        `Supprimer le prérequis ${preAcronym} → ${this.course.acronym} ?`
      )
      if (!confirmed) return

      try {
        await axios.delete(
          `http://localhost:8080/api/prerequisites?from=${preAcronym}&to=${this.course.acronym}`
        )

        this.successMessage = `Prérequis supprimé : ${preAcronym} → ${this.course.acronym}`
        this.prerequisites = this.prerequisites.filter(p => p.acronym !== preAcronym)

        this.$nextTick(this.renderGraph)
        setTimeout(() => (this.successMessage = ''), 2500)
      } catch (err) {
        console.error(err)
        this.error = 'Erreur lors de la suppression du prérequis.'
      }
    },

    renderGraph() {
      const container = this.$refs.networkContainer
      if (!container) return

      if (this.network) this.network.destroy()

      const nodes = [
        {
          id: this.course.acronym,
          label: this.course.acronym,
          color: '#10B981',
          font: { color: '#fff' }
        },
        ...this.prerequisites.map(pre => ({
          id: pre.acronym,
          label: pre.acronym,
          color: '#34D399',
          font: { color: '#fff' }
        }))
      ]

      const edges = this.prerequisites.map(pre => ({
        from: pre.acronym,
        to: this.course.acronym,
        arrows: 'to',
        color: { color: '#6EE7B7' }
      }))

      const data = { nodes, edges }
      const options = {
        nodes: {
          shape: 'circle',
          size: 16,
          borderWidth: 1,
          shadow: true
        },
        edges: {
          smooth: true,
          arrows: { to: { enabled: true, scaleFactor: 1 } },
          color: '#6EE7B7'
        },
        physics: {
          enabled: true,
          stabilization: { iterations: 100 }
        },
        interaction: {
          hover: true,
          dragNodes: true,
          zoomView: true,
          dragView: true
        },
        layout: { improvedLayout: true }
      }

      this.network = new Network(container, data, options)
    }
  }
}
</script>

<template>
  <div class="max-w-2xl mx-auto bg-black/40 rounded-2xl p-8 border border-gray-800 text-gray-200 shadow-lg">
    <h2 class="text-3xl font-semibold text-emerald-400 text-center mb-8">
      Détails du cours
    </h2>

    <section class="grid grid-cols-1 md:grid-cols-2 gap-10">
      <!-- Course Info -->
      <div class="space-y-3">
        <p><span class="text-gray-400">Acronyme :</span> <strong class="text-gray-100">{{ course.acronym }}</strong></p>
        <p><span class="text-gray-400">Titre :</span> <strong class="text-gray-100">{{ course.title }}</strong></p>
        <p><span class="text-gray-400">ECTS :</span> <strong class="text-gray-100">{{ course.ects }}</strong></p>
        <p><span class="text-gray-400">Bloc :</span> <strong class="text-gray-100">{{ course.block }}</strong></p>
        <p><span class="text-gray-400">Professeur :</span> <strong class="text-gray-100">{{ course.professor }}</strong></p>

        <h3 class="text-xl font-semibold text-emerald-400 mb-4 border-b border-gray-700 pb-2">
          Prérequis
        </h3>

        <div v-if="loading" class="text-gray-500 italic">Chargement...</div>
        <div v-else-if="error" class="text-red-500">{{ error }}</div>

        <!-- List of prerequisites -->
        <ul v-else-if="prerequisites.length" class="space-y-2 mb-4">
          <li
            v-for="pre in prerequisites"
            :key="pre.acronym"
            class="flex justify-between items-center bg-gray-800/60 px-4 py-2 rounded-md hover:bg-gray-800 transition"
          >
            <span>{{ pre.acronym }}</span>
            <button
              @click="removePrerequisite(pre.acronym)"
              class="text-red-400 hover:text-red-600 active:scale-95 transition"
              title="Supprimer ce prérequis"
            >
              <IconTrash />
            </button>
          </li>
        </ul>

        <p v-else class="text-gray-500 italic">Aucun prérequis défini pour ce cours.</p>
        <p v-if="successMessage" class="text-green-400 text-center font-medium">
          {{ successMessage }}
        </p>
      </div>

      <!-- Graph -->
      <div>
        <div
          v-if="prerequisites.length"
          ref="networkContainer"
          class="mt-4 h-64 bg-gray-900/40 rounded-lg border border-gray-700"
        ></div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.vis-network {
  border-radius: 0.5rem;
}
</style>    
