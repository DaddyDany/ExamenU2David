<template>
  <div
    class="container"
    style="height: 100vh"
  >
  <Carousel v-show="showElement"></Carousel>
    <div style="display: flex; margin-top: 50px; padding-bottom: 20px">
      <h1 style="margin-bottom: 0">LIBRERIA</h1>
      <Modal @registroExitoso="actualizarLibros"></Modal>
    </div>

    <div style="margin-bottom: 50px; width: 50%">
      <div>
        <button
          @click="ordenarLibrosDesc"
          style="
            padding: 10px;
            border-radius: 8px;
            width: 50%;
            margin-top: 20px;
          "
        >
          Ordenar por fecha
        </button>

         <button
          @click="ordenarLibrosAutor"
          style="
            padding: 10px;
            border-radius: 8px;
            width: 50%;
            margin-top: 20px;
          "
        >
          Ordenar por autor
        </button>
      </div>
    </div>
    <div class="text-center" id="loaderSpinner" v-if="loading">
      <b-spinner class="spinner" type="grow"></b-spinner>
      <b-spinner class="spinner" type="grow"></b-spinner>
      <b-spinner class="spinner" type="grow"></b-spinner>
      <b-spinner class="spinner" type="grow"></b-spinner>
      <b-spinner class="spinner" type="grow"></b-spinner>
    </div>
    <div v-if="books">
      <TransitionGroup name="zoomDown" tag="div" class="row">
        <div
          class="col-4 bookCards"
          v-for="book in books"
          :key="book.id"
        >
          <div>
            <b-card
              :title="book.name"
              img-src="https://picsum.photos/600/300/?image=1"
              img-alt="Image"
              img-top
              tag="article"
              style="max-width: 20rem"
              class="mb-2"
            >
              <b-card-text class="description">
                <h2>Autor: {{ book.autor }}</h2>
              </b-card-text>
              <div>
                <p>
                  <b>Fecha de publicación: </b
                  >{{ dateFormat(book.fecha) }}
                </p>
              </div>
              <div style="width: 100%; display: flex">
                <div
                  style="
                    display: flex;
                    width: 100%;
                    justify-content: space-around;
                  "
                >
                  <b-button
                    variant="danger"
                    style="width: 45%; padding: 8px"
                    @click="deleteLibro(book.id)"
                    >Eliminar</b-button
                  >
                  <h1>EditModal</h1>
                </div>
              </div>
            </b-card>
          </div>
        </div>
      </TransitionGroup>
    </div>
    <div v-else>
      <h3 class="text-center">Sin resultados</h3>
    </div>
  </div>
</template>

<script>
import Modal from "./Modal.vue";
import EditModal from "./EditModal.vue";
import Carousel from "./Carousel.vue"
import axios from "axios";

export default {
  name: "MovieCards",
  components: {
    Modal,
    EditModal,
    Carousel
  },
  data() {
    return {
      books: [],
      loading: null,
      search: null,
      endDate: null,
      startDate: null,
       lastScrollPosition: 0, // Valor inicial de la última posición de desplazamiento
    scrollDistance: 0, // Valor inicial de la distancia de desplazamiento
    showElement: true, // Valor inicial para mostrar el elemento
    
      loaded: false,
    };
  },
  methods: {
    actualizarLibros() {
      axios
        .get("http://localhost:8080/api/library")
        .then((response) => {
          this.books = response.data;
          this.loading = false;
        })
        .catch((e) => {
          console.error("Error en la peticion: ", e);
        });
    },
    ordenarLibrosDesc() {
      this.loading = true;
      axios
        .get("http://localhost:8080/api/library/latest")
        .then((response) => {
          this.books = response.data;
          this.loading = false;
        })
        .catch((e) => {
          console.error("Error en la peticion: ", e);
        });
    },
    ordenarLibrosAutor() {
      this.loading = true;
      axios
        .get("http://localhost:8080/api/library/autorAsc")
        .then((response) => {
          this.books = response.data;
          this.loading = false;
        })
        .catch((e) => {
          console.error("Error en la peticion: ", e);
        });
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
              this.actualizarLibros();
            })
            .catch((error) => {
              console.error(error);
            });
        }
      });
    },
    dateFormat(date) {
      let dateParts = date.split("-");
      let year = dateParts[0];
      let month = dateParts[1];
      let day = dateParts[2];
      let newDate = day + "-" + month + "-" + year;
      return newDate;
    },
   onScroll() {
  const currentScrollPosition = window.pageYOffset || document.documentElement.scrollTop;
  
  // Validar si el usuario se está desplazando hacia arriba o hacia abajo
  if (currentScrollPosition < this.lastScrollPosition) {
    // Si el usuario se desplaza hacia arriba, resetear la distancia de desplazamiento
    this.scrollDistance = 0;
    // Volver a mostrar el elemento si estaba oculto
    if (!this.showElement) {
      this.showElement = true;
    }
  } else {
    // Si el usuario se desplaza hacia abajo, aumentar la distancia de desplazamiento
    this.scrollDistance += Math.abs(currentScrollPosition - this.lastScrollPosition);
    
    // Ocultar el elemento si la distancia de desplazamiento supera cierto umbral
    if (this.scrollDistance > 100) { // Cambia 100 al valor deseado
      this.showElement = false;
    }
  }
  
  this.lastScrollPosition = currentScrollPosition;
}
  },
  mounted() {
    this.loading = true;
        window.addEventListener("scroll", this.onScroll);

      axios
        .get("http://localhost:8080/api/library")
        .then((response) => {
          this.books = response.data;
        })
        .catch((e) => {
          console.error("Error en la peticion: ", e);
        })
        .finally(() => {
          this.loading = false;
        });
  },
  beforeDestroy() {
    window.removeEventListener("scroll", this.onScroll);
  },
};
</script>

<style>
.bookCards {
  display: flex;
  justify-content: center;
}
#loaderSpinner {
  height: 75%;
  display: flex;
  justify-content: center;
  align-items: center;
}
.spinner {
  margin: 7px;
}

.hidden {
  display: none;
}
</Style>