<template>
  <div class="page meal-plan-schedule">
    <FullCalendar
      v-show="!isAddMealPlanOpen"
      ref="fullCalendar"
      class="demo-app-calendar"
      :options="calendarOptions"
    >
      <template v-slot:eventContent="arg">
        <!-- <b>{{ arg.timeText }}</b> -->
        <div>
          <router-link
            v-bind:to="{
              name: 'mealPlanDetails',
              params: { id: arg.event.extendedProps.mealPlanId },
            }"
            >{{ arg.event.title }}</router-link
          >
        </div>
        <div>
          <a v-on:click.prevent="noop">Remove</a>
        </div>
      </template>
    </FullCalendar>
    <div v-show="false" class="action-bar">
      <button class="btn" v-on:click.prevent="clearAll">Clear All</button>
    </div>

    <div class="card form add-meal-plan" v-if="isAddMealPlanOpen">
      <div class="field">
        <label for="searchMealPlan">Search MealPlans</label>
        <search-autocomplete
          id="searchMealPlan"
          v-bind:search-value="searchTerm"
          v-bind:get-data="getMealPlans"
          v-on:result="addMealPlan"
          v-on:change="setSearchTerm"
          v-bind:use-add="false"
        />
        <div class="form-controls align-right">
          <button class="btn btn-sm" v-on:click.prevent="closeMealPlanSearch">
            Cancel
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import FullCalendar from "@fullcalendar/vue";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";

import mealPlannerService from "@/services/MealPlannerService";
import SearchAutocomplete from "@/components/SearchAutocomplete";

export default {
  name: "meal-plan-schedule",
  components: {
    SearchAutocomplete,
    FullCalendar, // make the <FullCalendar> tag available
  },
  data() {
    return {
      searchTerm: "",
      mealPlans: [],
      isAddMealPlanOpen: false,
      calendarOptions: {
        plugins: [
          dayGridPlugin,
          timeGridPlugin,
          interactionPlugin, // needed for dateClick
        ],
        headerToolbar: {
          // left: "prev,next today",
          left: "prev",
          center: "title",
          right: "next",
          // right: "dayGridMonth,timeGridWeek,timeGridDay",
        },
        initialView: "dayGridMonth",
        initialEvents: JSON.parse(localStorage.getItem("scheduledMealPlans")), // alternatively, use the `events` setting to fetch from a feed
        editable: true,
        selectable: true,
        selectMirror: true,
        dayMaxEvents: true,
        weekends: true,
        select: this.handleDateSelect,
        eventClick: this.handleEventClick,
        eventsSet: this.handleEvents,
        /* you can update a remote database when these fire:
        eventAdd:
        eventChange:
        eventRemove:
        */
      },
    };
  },
  methods: {
    handleDateSelect(selectInfo) {
      if (this.isAddMealPlanOpen) {
        return;
      }
      this.openMealPlanSearch(selectInfo);
    },
    noop() {
      // TODO figure out the API to programmitically remove the element
    },
    handleEventClick(clickInfo) {
      if (
        confirm(
          `Are you sure you want to remove the meal plan '${clickInfo.event.title}'`
        )
      ) {
        clickInfo.event.remove();
      }
    },
    handleEvents(events) {
      console.log("events", events);
      localStorage.setItem("scheduledMealPlans", JSON.stringify(events));
      setTimeout(() => {
        try {
          this.$refs.fullCalendar.getApi().render();
        } catch (e) {
          console.warn(e);
        }
      }, 10);
    },
    async getMealPlans() {
      const response = await mealPlannerService.getMealPlans();
      return response.data;
    },
    openMealPlanSearch(selectInfo) {
      this.selectInfo = selectInfo;
      this.isAddMealPlanOpen = true;
    },
    closeMealPlanSearch() {
      this.isAddMealPlanOpen = false;
      setTimeout(() => {
        try {
          this.$refs.fullCalendar.getApi().render();
        } catch (e) {
          console.warn(e);
        }
      }, 10);
    },
    setSearchTerm(searchTerm) {
      this.searchTerm = searchTerm;
    },
    addMealPlan(mealPlan) {
      let calendarApi = this.selectInfo.view.calendar;
      calendarApi.unselect(); // clear date selection
      if (mealPlan) {
        calendarApi.addEvent({
          id: this.selectInfo.startStr + mealPlan.mealPlanId,
          mealPlanId: mealPlan.mealPlanId,
          title: mealPlan.name,
          start: this.selectInfo.startStr,
          end: this.selectInfo.endStr,
          allDay: this.selectInfo.allDay,
        });
      }
      this.selectInfo = null;
      this.closeMealPlanSearch();
    },
    clearAll() {
      localStorage.setItem("scheduledMealPlans", JSON.stringify([]));
    },
  },
};
</script>

<style>
div.meal-plan-schedule {
  background-color: rgb(245, 245, 245);
}
.fc {
  /* the calendar root */
  max-width: 1100px;
  margin: 0 auto;
  width: 80%;
}
</style>