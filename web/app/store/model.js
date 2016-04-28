/* global _ */
import Ember from 'ember';
import Base from './mixin/base';
import ExpiringDate from './mixin/expiring-data';

import defineCollection from './collection';

var modelObject = Ember.Object.extend(Ember.Evented, Base, ExpiringDate, {
    shouldLoad: function() {
        return !this.get('isLoaded') || this.isExpired();
    },

    reload: function(returnPromise) {
        this.set('isLoaded', false);
        return this.load(returnPromise);
    },

    empty: function() {
        this.set('content', undefined);
    },

    rollback: function() {
        this.setProperties(Ember.$.extend(true, {}, this.get('rollbackData')));
    }
});

var modelPropertiesFactory = function(properties) {
    return {
        instances: {},

        create: function(data, flag) {
            if(flag) {
                return this._super.apply(this, [data]);
            }

            var instance;

            if(properties.deserialize) {
                data = properties.deserialize(data);
            }

            Ember.assert('Data must have an id field', data.id);

            var instanceId = this.modelId({id: data.id});

            if(this.instances[instanceId]) {
                instance = this.instances[instanceId];
                this.instances[instanceId].setProperties(data);
            } else {
                instance = this._super(data);
                this.instances[instanceId] = instance;
            }

            instance.set('isLoaded', true);
            instance.set('rollbackData', Ember.$.extend(true, {}, data));

            return instance;
        }
    };
};

var modelProperties = {
    defineCollection: defineCollection,

    modelId: function(options) {
        var format = function(e) {
            return JSON.stringify(_.sortBy(_.pairs(e), function(e) {
                return e[0];
            }));
        };

        return "__" + format(options || {});
    },

    find: function(options) {
        var modelId = this.modelId(options);
        var instance = this.instances[modelId];

        if(!instance) {
            instance = this.instances[modelId] = this.create(options || {}, true);
        }

        return instance;
    }
};

export default {
    defineModel: function(properties) {
        var args = Array.prototype.slice.call(arguments, 0);
        properties = args[args.length - 1];
        var model =  modelObject.extend.apply(modelObject, args);

        model.reopenClass(modelPropertiesFactory(properties));
        model.reopenClass(modelProperties);

        return model;
    }
};
