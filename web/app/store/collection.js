/* global _ */
import Ember from 'ember';
import Base from './mixin/base';
import ExpiringDate from './mixin/expiring-data';

var collectionObject = Ember.ArrayProxy.extend(Base, ExpiringDate, {
    isEmpty: function() {
        return this.get('length') === 0;
    }.property('length'),

    shouldLoad: function() {
        return this.get('isEmpty') || this.get('moreToLoad') || this.isExpired();
    },

    empty: function() {
        this.clear();
        return this;
    },

    setContent: function(items, flag) {
        var self = this;
        this.addObjects(_.map(items, function(item) {
            return self.model.create(item, flag);
        }));

        return this;
    }
});

var collectionPropertiesFactory = function() {
    return {
        collections: {}
    };
};

var collectionProperties = {
    collectionId: function(type, options) {
        var format = function(e) {
            return JSON.stringify(_.sortBy(_.pairs(e), function(e) {
                return e[0];
            }));
        };

        return "__" + format(options || {});
    },

    find: function(options) {
        var collectionId = this.collectionId(this.type, options);
        var instance = this.collections[collectionId];

        if (!instance) {
            options = options || {};
            options.content = Ember.A();

            instance = this.collections[collectionId] = this.create(options || {});
        }

        return instance;
    }
};

export default function(data) {
    var self = this;
    var collection = collectionObject.extend(_.defaults({model: self}, data));

    collection.reopenClass(collectionPropertiesFactory());
    collection.reopenClass(collectionProperties);

    return collection;
}

