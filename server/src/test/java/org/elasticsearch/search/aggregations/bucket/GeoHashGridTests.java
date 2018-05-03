/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.search.aggregations.bucket;

import org.elasticsearch.search.aggregations.BaseAggregationTestCase;
import org.elasticsearch.search.aggregations.bucket.geogrid.GeoGridAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.geogrid.GeoHashType;

public class GeoHashGridTests extends BaseAggregationTestCase<GeoGridAggregationBuilder> {

    @Override
    protected GeoGridAggregationBuilder createTestAggregatorBuilder() {
        String name = randomAlphaOfLengthBetween(3, 20);
        GeoGridAggregationBuilder factory = new GeoGridAggregationBuilder(name);
        if (randomBoolean()) {
            factory.type(randomFrom(GeoHashType.values()).toString());
        }
        if (randomBoolean()) {
            int precision;
            switch (factory.type()) {
                case GEOHASH:
                    precision = randomIntBetween(1, 12);
                    break;
                case PLUSCODE:
                    precision = randomFrom(4, 6, 8, 10, 11, 12, 13, 14);
                    break;
                default:
                    throw new IllegalArgumentException(
                        "GeoHashType." + factory.type().name() + " was not added to the test");
            }
            factory.precision(precision);
        } else {
            // precision gets initialized during the parse stage, so we have to force it
            // to the default value for the chosen hashing type.
            factory.precision(factory.type().getHandler().getDefaultPrecision());
        }
        if (randomBoolean()) {
            factory.size(randomIntBetween(1, Integer.MAX_VALUE));
        }
        if (randomBoolean()) {
            factory.shardSize(randomIntBetween(1, Integer.MAX_VALUE));
        }
        return factory;
    }

}
