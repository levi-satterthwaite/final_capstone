<template>
  <div class="autocomplete">
    <input
      type="text"
      autocomplete="off"
      v-model="searchTerm"
      v-bind:id="id"
      v-on:input.prevent="onInput"
      v-on:keydown.down.prevent="onArrowDown"
      v-on:keydown.up.prevent="onArrowUp"
      v-on:keydown.enter.prevent="onEnter"
    />
    <ul class="autocomplete-results" v-show="isOpen">
      <li class="loading" v-if="isLoading">Searching...</li>
      <li
        class="autocomplete-result"
        v-else
        v-for="(result, i) in results"
        v-bind:key="i"
        v-bind:class="{ 'is-active': i === arrowCounter }"
        v-on:click.prevent="setResult(result)"
      >
        {{ result.name }}
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: "search-autocomplete",
  // props are the arguments that get passed to the component when the 
  // component is used in the template 
  props: {
    id: null,
    getData: {
      type: Function,
      required: true,
    },
  },
  // data() returns the component's state variables 
  data() {
    return {
      searchTerm: "",
      results: [],
      // toggle for deciding whether we display results on the screen or not
      isOpen: false,
      // current highlighted result item 
      arrowCounter: -1,
      isLoading: false,
      isLoaded: false,
      error: null
    };
  },
  methods: {
    filterResults() {
      // iterating over every item and checking if item has the search term
      // in order to filter the results
      this.results = this.items.filter((item) => {
        return item.name.toLowerCase().indexOf(this.searchTerm.toLowerCase()) > -1;
      });
    },
    async onInput() {
      // triggers a change event that allows parent component(s) to track
      // the search term
      this.$emit('change', this.searchTerm);
      // if we are already loading data from the getData function
      // just return, because we still need to wait for the data
      if (this.isLoading) {
        return; // stop the function
      }

      // if we have not loaded data then we will load the data
      if (!this.isLoaded) {
        // since we are about to start loading we need to set isLoading to true
        this.isLoading = true;
        // we need to get the data. getData could return promise. Since it could return
        // a promise, we are using async / await. This will help us keep the code
        // cleaner and simpler.
        try {
          this.items = await this.getData();
        } catch (error) {
          // if we get an error we will store it into the component so 
          // we can display the error message to the user
          this.error = error;
        }
        // now that we are done loading we need to set the isLoading to false and
        // set isLoaded true
        this.isLoading = false;
        this.isLoaded = true;
      }

      // since we are not loading data and data has been loaded we can filter what
      // the user sees based on what the user has typed
      this.filterResults();

      // we need to make sure the the results can be displayed so we are 
      // setting isOpen true
      this.isOpen = true;
    },
    setResult(result) {
      this.searchTerm = '';
      this.selectedResult = result;
      this.isOpen = false;
      // emit (triggers any v-on:result properties set in the template) 
      // the result so the parent component can do something
      // with the result the user selected
      this.$emit('result', result);
    },
    handleClickOutside(event) {
      // $el is the html element associated with this component
      // if the html element doesn't contain the event target, close the results
      if (!this.$el.contains(event.target)) {
        this.arrowCounter = -1;
        this.isOpen = false;
      }
    },
    onArrowDown() {
      if (this.arrowCounter < this.results.length) {
        this.arrowCounter = this.arrowCounter + 1;
      }
    },
    onArrowUp() {
      if (this.arrowCounter > 0) {
        this.arrowCounter = this.arrowCounter - 1;
      }
    },
    onEnter() {
      // if the user hits enter, grab the result from where the arrowCounter is
      // currently set
      const result = this.results[this.arrowCounter];
      this.arrowCounter = -1;
      this.setResult(result);
    },
  },
  mounted() {
    // document is a global variable that represents/contains all the html element
    // when the component gets created we need to add the listener
    document.addEventListener("click", this.handleClickOutside);
  },
  destroyed() {
    // when the component gets removed, we need to remove the listener
    document.removeEventListener("click", this.handleClickOutside);
  },
};
</script>

<style>
.autocomplete {
  position: relative;
}

.autocomplete-results {
  padding: 0;
  margin: 0;
  border: 1px solid #eeeeee;
  height: 120px;
  min-height: 1em;
  max-height: 6em;
  overflow: auto;
}

.autocomplete-result {
  list-style: none;
  text-align: left;
  padding: 4px 2px;
  cursor: pointer;
}

.autocomplete-result:hover {
  background-color: #4aae9b;
  color: white;
}

.autocomplete-result.is-active,
.autocomplete-result:hover {
  background-color: #4aae9b;
  color: white;
}
</style>