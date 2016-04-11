import Ember from 'ember';

export default Ember.Mixin.create({
    _expiration: 1,

    init() {
        this._super();
        if (this.expiration) {
            this.setExpirationInterval(this.expiration);
        }
    },

    setExpirationInterval(interval) {
        this.set('_expiration', interval);
    },

    justGotData() {
        this.set('_lastUpdateTime', (new Date()).getTime());
    },

    isExpired() {
        return this.get('_expiration') ? (new Date()).getTime() - (this.get('_lastUpdateTime') || 0) > this.get('_expiration') : false;
    }
});
