
// ------------------------- Models ------------------------------

// Model for a single Note
window.Note = Backbone.Model.extend({
  defaults: {
    text: '',
    editing: 'false'
  }
});

// Model for a collection of Notes
window.Notes = Backbone.Collection.extend({
  model: Note,
  url: '/api/notes'
});

// ------------------------- Views------------------------------

// Single Note within NotesView
window.NoteView = Backbone.View.extend({
  template : _.template($('#note-template').html()),

  initialize: function() {
    this.model.on('change', this.render, this);
    this.model.on('destroy', this.remove, this);
  },

  events: {
    'click #delete-note': 'deleteNote',
    'click #edit-note': 'editNote',
    'click #save-edit-note': 'saveEdit',
    'click #cancel-edit-note': 'cancelEdit'
  },

  render: function(){
    this.$el.html(this.template(this.model.toJSON()));
    return this;
  },

  remove: function(){
      this.$el.remove();
  },

  deleteNote: function() {
    this.model.destroy({wait: true});
  },

  editNote: function() {
    this.model.set({editing : 'true'});
  },

  saveEdit: function() {
    this.model.set({
        text: $('#edit-note-text').val(),
        editing: 'false'
    });
    this.model.save();
  },

  cancelEdit: function() {
    this.model.set({editing : 'false'});
  }


});

// Notes view
window.NotesView = Backbone.View.extend({

  template : _.template($('#note-list-template').html()),

  initialize: function(){
    this.collection.on('add', this.addOne, this);
    this.collection.on('reset', this.addAll, this);
  },

  events: {
    'click #create-note': 'createNote'
  },

  render: function(){
    this.$el.empty();
    this.$el.html(this.template());

    this.addAll()
    return this;
  },

  addAll: function(){
    this.collection.forEach(this.addOne, this);
  },

  addOne: function(note){
    var noteView = new NoteView({model: note});
    this.$el.append(noteView.render().el);
  },

  createNote: function() {
    this.collection.create({text: $('#note-text').val()}, {wait: true});
    $('#note-text').val('');
    $('#note-text').focus();
  }

});
