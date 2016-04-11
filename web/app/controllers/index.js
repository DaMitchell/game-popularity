import Ember from 'ember';

export default Ember.Controller.extend({
    actions: {
        emptyGames() {
            this.get('model').empty();
        }
    }
});
