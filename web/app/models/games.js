import Ember from 'ember';
import GameModel from './game';

export default GameModel.defineCollection({
    name: '',

    request() {
        var params = {};

        if(this.get('name').length >= 3) {
            params.name = this.get('name');
        }

        return Ember.$.get('http://localhost:8080/games', params);
    },

    afterSuccess(data) {
        if(data['_embedded'] && data['_embedded']['games']) {
            this.setContent(data['_embedded']['games']);
        }
    }
});
