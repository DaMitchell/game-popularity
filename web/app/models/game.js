import Ember from 'ember';
import StoreModel from '../store/model';

import config from '../config/environment';

export default StoreModel.defineModel({

    request() {
        return Ember.$.get('http://' + config.apiBaseURL + '/games/' + this.get('id'));
    },

    afterSuccess(data) {
        this.setProperties(data);
    },

    afterFail(error) {
        console.log(error);
    }
});
