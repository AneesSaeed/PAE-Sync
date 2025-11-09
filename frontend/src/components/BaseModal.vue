<script>
export default {
  name: 'BaseModal',
  props: {
    modelValue: Boolean
  },
  emits: ['update:modelValue']
}
</script>

<template>
  <div
    v-if="modelValue"
    class="fixed inset-0 flex justify-center items-center  bg-gray-950/70 backdrop-blur-sm transition-opacity duration-300"
    @click="$emit('update:modelValue', false)"
  >
    <div
      class="relative"
      @click.stop
    >
      <button
        @click="$emit('update:modelValue', false)"
        class="absolute top-2 right-3 text-gray-400 hover:text-emerald-400 text-3xl font-light transition"
      >
        ×
      </button>
      <slot />
    </div>
  </div>
</template>