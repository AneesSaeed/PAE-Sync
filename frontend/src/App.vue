<script>
export default {
  data() {
    return {
      cursorX: 0,
      cursorY: 0,
      isInteractive: false,
      cursorColor: '#00ff00',
    }
  },
  methods: {
    handleMouseMove(e) {
      this.cursorX = e.clientX
      this.cursorY = e.clientY

      const target = e.target

      // Detect clickable or interactive elements
      const interactive =
        target.closest('button') ||
        target.closest('a') ||
        target.closest('table') ||
        target.closest('input') ||
        target.closest('select')

      this.isInteractive = !!interactive
    },
  },
}
</script>

<template>
  <div
    class="relative min-h-screen overflow-hidden"
    @mousemove="handleMouseMove"
  >
    <!-- Custom cursor -->
    <div
      class="pointer-events-none fixed top-0 left-0 z-50"
      :style="{
        width: isInteractive ? '48px' : '12px',
        height: isInteractive ? '48px' : '12px',
        borderRadius: '1111px',
        backgroundColor: isInteractive ? 'transparent' : cursorColor,
        border: isInteractive ? `2px solid ${cursorColor}` : 'none',
        boxShadow: isInteractive
          ? `0 0 25px 4px ${cursorColor}80`
          : `0 0 10px 2px ${cursorColor}70`,
        transform: `translate(calc(${cursorX}px - 50%), calc(${cursorY}px - 50%))`,
        transition: 'width 0.2s ease-out, height 0.2s ease-out, background-color 0.2s ease-out, border 0.2s ease-out, box-shadow 0.2s ease-out',
      }"
    ></div>

    <router-view />
  </div>
</template>