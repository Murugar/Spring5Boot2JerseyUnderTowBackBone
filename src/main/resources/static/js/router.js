window.SampleApp = new (Backbone.Router.extend({
  routes: {
    "": "index",
  },

  initialize: function(){
    this.notes = new Notes();
    this.notesView = new NotesView({collection: this.notes});
    this.notesView.render();
  },

  index: function(){
    $('#app').html(this.notesView.el);
    this.notes.fetch();
  },

  start: function(){
    Backbone.history.start();
  }

}));