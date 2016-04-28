import Ember from 'ember';
import GameModel from './game';

import config from '../config/environment';

export default GameModel.defineCollection({
    name: '',

    total: 888,

    request() {
        var params = {};

        if(this.get('name').length >= 3) {
            params.name = this.get('name');
        }

        return Ember.$.get('http://' + config.apiBaseURL + '/games', params);
    },

    afterSuccess(data) {
        if(data['total']) {
            this.set('total', data['total']);
        }

        if(data['_embedded'] && data['_embedded']['games']) {
            this.setContent(data['_embedded']['games']);
        }
    }
});
