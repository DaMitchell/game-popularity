import Ember from 'ember';

export default Ember.Component.extend({
    isActive: false,

    tagName: 'button',
    classNames: ['active-button'],
    classNameBindings: ['isActive:active'],

    click() {
        this.sendAction('change', this.toggleProperty('isActive'));
    }
});
