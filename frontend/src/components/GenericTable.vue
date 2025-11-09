<script>
import IconTrash from '@/components/icons/IconTrash.vue'

export default {
    name: 'GenericTable',
    components: { IconTrash },
    props: {
        headers: Array,
        rows: Array,
        columns: Array,
    },
    emits: ['delete-row', 'row-clicked'],
}
</script>

<template>
    <table class="w-full rounded-xl overflow-hidden shadow-lg bg-black/40">
        <thead>
            <tr class="bg-emerald-700/80 text-white">
                <!-- Normal headers -->
                <th v-for="(header, index) in headers" :key="index"
                    class="p-3 text-left font-semibold text-sm uppercase tracking-widest">
                    {{ header }}
                </th>

                <!-- Extra header for delete column -->
                <th class="p-3 text-center w-16"></th>
            </tr>   
        </thead>

        <tbody class="divide-y divide-gray-800">
            <template v-if="rows && rows.length">
                <tr
                v-for="(row, rowIndex) in rows"
                :key="rowIndex"
                class="hover:bg-emerald-800/40 transition-colors duration-200 cursor-pointer"
                @click="$emit('row-clicked', row)"
                >
                    <td
                        v-for="(column, colIndex) in columns"
                        :key="colIndex"
                        class="p-3 text-sm text-gray-200"
                    >
                        {{ row[column] }}
                    </td>
                    <!-- Delete icon -->
                    <td class="p-3 text-center align-middle">
                        <button @click.stop="$emit('delete-row', row)" class="rounded-full text-red-500 hover:text-red-400 hover:bg-red-500/10 
                     transition-all duration-200 active:scale-95 flex items-center justify-center mx-auto w-9 h-9"
                            aria-label="Supprimer le cours">
                            <IconTrash />
                        </button>
                    </td>
                </tr>
            </template>

            <!-- No rows -->
            <tr v-else>
                <td :colspan="headers.length + 1" class="text-center text-gray-500 italic py-6">
                    Aucun cours disponible
                </td>
            </tr>
        </tbody>
    </table>
</template>
