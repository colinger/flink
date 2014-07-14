/***********************************************************************************************************************
 *
 * Copyright (C) 2010-2014 by the Stratosphere project (http://stratosphere.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 **********************************************************************************************************************/

package eu.stratosphere.streaming.test.batch.wordcount;

import eu.stratosphere.streaming.api.StreamRecord;
import eu.stratosphere.streaming.api.StreamRecordBatch;
import eu.stratosphere.streaming.api.invokable.UserTaskInvokable;
import eu.stratosphere.types.LongValue;
import eu.stratosphere.types.StringValue;

public class BatchWordCountSplitter extends UserTaskInvokable {

	private StringValue sentence = new StringValue("");
	private LongValue timestamp = new LongValue(0);
	private String[] words = new String[0];
	private StringValue wordValue = new StringValue("");
	private StreamRecord outputRecord = new StreamRecord(2);

	@Override
	public void invoke(StreamRecord record) throws Exception {
		StreamRecordBatch records=(StreamRecordBatch) record;
		int numberOfRecords = records.getNumOfRecords();
		for(int i=0; i<numberOfRecords; ++i){
			sentence = (StringValue) records.getField(i, 0);
			timestamp = (LongValue) records.getField(i, 1);
			words = sentence.getValue().split(" ");
			for (CharSequence word : words) {
				wordValue.setValue(word);
				outputRecord.setField(0, wordValue);
				outputRecord.setField(1, timestamp);
				emit(outputRecord);
			}
		}
	}
}