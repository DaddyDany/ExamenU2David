<template>
  <div
    class="scrollable-container"
    style="height: 100vh"
    @scroll="handleScroll"
  >
    <Carousel
      :class="{ hidden: !showElement, 'fade-transition': true }"
    ></Carousel>
    <div style="display: flex; margin-top: 100px; padding-bottom: 20px">
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
    <div class="row">
      <div class="col-9">
        <div v-if="books">
          <TransitionGroup name="zoomDown" tag="div" class="row">
            <div class="col-4 bookCards" v-for="book in books" :key="book.id">
              <div
                class="drag-item"
                draggable="true"
                @dragstart="handleDragStart($event, book.id)"
                @dragend="handleDragEnd(book.id)"
              >
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
                      <b>Fecha de publicación: </b>{{ dateFormat(book.fecha) }}
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
      <div class="col-3">
        <DeleteSquare @libroEliminado="actualizarLibros"></DeleteSquare>
        <EditSquare></EditSquare>
      </div>
    </div>
  </div>
</template>

<script>
import Modal from "./Modal.vue";
import EditModal from "./EditModal.vue";
import Carousel from "./Carousel.vue";
import DeleteSquare from "./DeleteSquare.vue";
import EditSquare from "./EditSquare.vue";
import axios from "axios";

export default {
  name: "MovieCards",
  components: {
    Modal,
    EditModal,
    Carousel,
    DeleteSquare,
    EditSquare,
  },
  data() {
    return {
      books: [],
      loading: null,
      search: null,
      endDate: null,
      startDate: null,
      showElement: true,
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
    handleDragStart(event, bookId) {
      event.dataTransfer.setData(
        "application/json",
        JSON.stringify({ bookId })
      );
    },
    handleDragEnd(id) {
      console.log("Drag done on element with id " + id);
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
    dateFormat(date) {
      let dateParts = date.split("-");
      let year = dateParts[0];
      let month = dateParts[1];
      let day = dateParts[2];
      let newDate = day + "-" + month + "-" + year;
      return newDate;
    },
    handleScroll() {
      const currentScrollPosition = window.scrollY;
      const scrollThreshold = 0;
      if (currentScrollPosition > scrollThreshold) {
        setTimeout(() => {
          this.showElement = false;
        }, 100);
      } else {
        setTimeout(() => {
          this.showElement = true;
        }, 100);
      }
      this.lastScrollPosition = currentScrollPosition;
    },
  },
  mounted() {
    this.loading = true;
    window.addEventListener("scroll", this.handleScroll);

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
    window.removeEventListener("scroll", this.handleScroll);
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

.scrollable-container {
  margin: 50px;
  height: auto;
  transition: height 0.3s ease;
  min-height: 100vh;
}

.fade-transition {
  transition: opacity 0.3s ease;
}

.hidden {
  opacity: 0;
  height: 10px;
  display: none;
}

.square {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 80%;
  margin-left: auto;
  height: 200px;
  border: 1px solid black;
  border-radius: 10px;
  margin-top: 100px;
}
</style>
