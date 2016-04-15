import Ember from 'ember';
import GameModel from './game';

import config from '../config/environment';

export default GameModel.defineCollection({
    name: '',

    request() {
        var params = {};

        if(this.get('name').length >= 3) {
            params.name = this.get('name');
        }

        return Ember.$.get('http://' + config.apiBaseURL + '/games', params);
    },

    afterSuccess(data) {
        if(data['_embedded'] && data['_embedded']['games']) {
            this.setContent(data['_embedded']['games'], true);
        }
    }
});
