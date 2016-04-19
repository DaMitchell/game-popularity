import Ember from 'ember';
import StoreModel from '../store/model';

import config from '../config/environment';

const { assert } = Ember;
const TYPES = [
    'day',
    'week',
    'month'
];

export default StoreModel.defineModel({

    request() {
        var url = 'http://' + config.apiBaseURL + '/games/' + this.get('id');
        var type = this.get('type');

        if(type) {
            if(TYPES.indexOf(type) < 0) {
                assert('You have provided an invalid type to the Game model.');
            } else {
                url += '/' + type;
            }
        }

        return Ember.$.get(url);
    },

    afterSuccess(data) {
        this.setProperties(data);
    },

    afterFail(error) {
        console.log(error);
    }
});
