import Ember from 'ember';
import StoreModel from '../store/model';

export default StoreModel.defineModel({
    request() {
        return Ember.$.get('http://localhost:8080/games/' + this.get('id'));
    },

    afterSuccess(data) {
        console.log(data);
        this.setProperties(data);
    },

    afterFail(error) {
        console.log(error);
    }
});
