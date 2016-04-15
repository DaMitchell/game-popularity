import Ember from 'ember';

export default Ember.Controller.extend({
    isRightOpen: false,

    actions: {
        toggleRight: function() {
            this.toggleProperty('isRightOpen');
        }
    }
});
