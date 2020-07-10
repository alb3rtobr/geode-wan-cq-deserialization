/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import java.util.LinkedList;
import java.util.Queue;

import org.apache.geode.cache.CacheListener;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.RegionEvent;
import org.apache.geode.cache.util.CacheListenerAdapter;
import org.apache.geode.pdx.PdxReader;
import org.apache.geode.pdx.PdxSerializable;
import org.apache.geode.pdx.PdxWriter;

public class ExamplePdxSerializable implements PdxSerializable {

  private int id;
  private String type;
  private String status;

  public ExamplePdxSerializable() {}

  @Override
  public void toData(PdxWriter writer) {
    writer.writeInt("id", id).markIdentityField("id").writeString("type", type)
        .writeString("status", status);
  }

  @Override
  public void fromData(PdxReader reader) {
    id = reader.readInt("id");
    type = reader.readString("type");
    status = reader.readString("status");
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public int getId() {
    return this.id;
  }

  public String getType() {
    return this.type;
  }

  public String getStatus() {
    return this.status;
  }

}
