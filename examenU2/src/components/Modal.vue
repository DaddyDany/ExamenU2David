<template>
  <div style="margin-left: auto">
    <b-button v-b-modal.modal-1>Agregar Libro</b-button>
    <b-modal
      id="modal-1"
      title="Agregar Libro"
      hide-footer
      @shown="clearErrors"
    >
      <template #modal-header="{ close }">
        <h5>Agregar Libro</h5>
        <b-button size="sm" variant="outline-danger" @click="close()">
          X
        </b-button>
      </template>
      <form id="form" @submit.prevent="checkForm">
        <div v-if="errors.length">
          <b-alert show variant="danger">
            <b>Por favor, corrija el(los) siguiente(s) error(es):</b>
            <ul>
              <li v-for="error in errors" :key="error">{{ error }}</li>
            </ul>
          </b-alert>
        </div>

        <div class="fieldContainer">
          <div class="labelContainer">
            <label for="name">Nombre: </label>
          </div>
          <div class="inputContainer">
            <input id="name" v-model="name" type="text" name="name" />
          </div>
        </div>

        <div class="fieldContainer">
          <div class="labelContainer">
            <label for="autor">Autor: </label>
          </div>
          <div class="inputContainer">
            <input id="autor" v-model="autor" type="text" name="autor" />
          </div>
        </div>

        <div class="fieldContainer">
          <div class="labelContainer">
            <label for="fecha">Fecha de publicación: </label>
          </div>
          <div class="inputContainer">
            <input
              id="fecha"
              v-model="fecha"
              name="fecha"
              type="date"
            />
          </div>
        </div>

        <div class="buttonsContainer">
          <button type="submit" id="botonEnviar">Enviar</button>
          <button type="button" @click="closeModal" id="botonCancelar">
            Cancelar
          </button>
        </div>
      </form>
    </b-modal>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "Modal",
  data() {
    return {
      errors: [],
      name: null,
      autor: null,
      fecha: null,
    };
  },
  methods: {
    checkForm() {
      if (
        this.name &&
        this.autor &&
        this.fecha
      ) {
        this.sendForm();
        return true;
      }
      this.errors = [];
      if (!this.name) {
        this.errors.push("El nombre es obligatorio.");
      }
      if (!this.autor) {
        this.errors.push("El autor es obligatorio.");
      }
      if (!this.fecha) {
        this.errors.push("La fecha de publicación es obligatoria");
      }
    },
    sendForm() {
      axios
        .post("http://localhost:8080/api/library", {
          name: this.name,
          autor: this.autor,
          fecha: this.fecha,
        })
        .then((response) => {
          this.name = null;
          this.autor = null;
          this.fecha = null;
          this.errors = [];
          this.$emit("registroExitoso");
          this.$swal({
            title: "Agregada",
            text: "El libro ha sido agregado con exito",
            icon: "success",
          });
          this.$root.$emit("bv::hide::modal", "modal-1");
        })
        .catch((error) => {
          console.log(error);
        });
    },
    closeModal() {
      this.$root.$emit("bv::hide::modal", "modal-1");
      this.name = null;
      this.fecha = null;
      this.autor = null;
    },
    clearErrors() {
      this.errors = [];
    },
  },
};
</script>

<style>

</style>