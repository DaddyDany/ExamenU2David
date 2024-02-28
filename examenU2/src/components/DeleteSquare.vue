<template>
  <div class="square" @drop="handleDrop" @dragover.prevent>
    <h2>Eliminar</h2>
  </div>
</template>

<script>
import axios from "axios";
export default {
  methods: {
    handleDrop(e) {
      e.preventDefault();
      const object = JSON.parse(e.dataTransfer.getData("application/json"));
      const id = object.bookId;
      this.deleteLibro(id);
    },
    deleteLibro(id) {
      this.$swal({
        title: "¿Estas seguro?",
        text: "No podras revertir este cambio",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        cancelButtonText: "cancelar",
        confirmButtonText: "Si, eliminar",
      }).then((result) => {
        if (result.isConfirmed) {
          axios
            .delete(`http://localhost:8080/api/library/${id}`)
            .then((response) => {
              this.$swal({
                title: "Eliminada",
                text: "El libro ha sido eliminado con exito",
                icon: "success",
              });
              this.$emit("libroEliminado");
            })
            .catch((error) => {
              console.error(error);
            });
        }
      });
    },
  },
};
</script>

<style></style>
