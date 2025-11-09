<script>
export default {
  name: "SearchBar",
  
  props: {
    modelValue: String,
    placeholder: {
      type: String,
      default: "Search a course",
    },
  },
  
  data() {
    return {
        localSearch: this.modelValue,
    };
  },
  
  watch: {
    modelValue(newVal) {
        this.localSearch = newVal;
    },
    localSearch(newVal) {
        this.$emit("update:modelValue", newVal);
    },
  },

  mounted() {
    // Alt + L shortcut to focus the input
    window.addEventListener("keydown", this.focusShortcut);
  },

  beforeUnmount() {
    window.removeEventListener("keydown", this.focusShortcut);
  },

  methods: {
    focusShortcut(e) {
      const input = document.getElementById("course-search");
      if (e.altKey && e.key.toLowerCase() === "l" && document.activeElement !== input) {
        e.preventDefault();
        input.focus();
      }
    },
  },
};
</script>

<template>
  <div class="flex justify-center mb-8">
    <div class="relative w-[65%] max-w-2xl">
      <!-- Magnifying glass icon -->
      <svg
        xmlns="http://www.w3.org/2000/svg"
        class="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400 pointer-events-none"
        fill="none"
        viewBox="0 0 24 24"
        stroke="currentColor"
        stroke-width="2"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          d="M21 21l-4.35-4.35M10 18a8 8 0 100-16 8 8 0 000 16z"
        />
      </svg>

      <!-- Input -->
      <input
        id="course-search"
        v-model="localSearch"
        type="text"
        :placeholder="placeholder"
        autocomplete="off"
        class="w-full pl-10 pr-16 p-3 rounded-lg bg-gray-800/80 text-gray-100 placeholder-gray-500
               border border-emerald-600/40 focus:outline-none focus:ring-2 focus:ring-emerald-500
               hover:shadow-[0_0_12px_rgba(16,185,129,0.25)]"
      />

      <!-- Shortcut hint -->
      <kbd
        class="absolute right-3 top-1/2 -translate-y-1/2 text-xs text-gray-400 bg-gray-700/60 
               border border-gray-600 rounded px-1.5 py-0.5 pointer-events-none select-none"
      >Alt + L</kbd>
    </div>
  </div>
</template>