/**
 *      Copyright 2020-2021 Daniel Sanchez
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.districtmeps.bot.objects;

public class SyncronizeObj {
    public void doWait(long l){
        synchronized(this){
            try {
                this.wait(l);
            } catch(InterruptedException e) {
            }
        }
    }
    
    public void doNotify() {
        synchronized(this) {
            this.notify();
        }
    }
    
    public void doWait() {
        synchronized(this){
            try {
                this.wait();
            } catch(InterruptedException e) {
            }
        }
    }
    }