package models;

import java.util.ArrayList;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class RecordConverter implements Converter{

	@Override
	public boolean canConvert(Class arg0) {
		return arg0.equals(Records.class);
	}

	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
		
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		Records record = new Records();
		ArrayList<FieldValues> fieldValues = new ArrayList<FieldValues>();
		record.setFieldValues(fieldValues);
		while(reader.hasMoreChildren()){
			reader.moveDown();
			if("values".equals(reader.getNodeName())){
				FieldValues fv = new FieldValues();
				reader.moveDown();
				if("value".equals(reader.getNodeName()))
					System.out.println("found value1" + reader.getValue());
				reader.moveDown();
				if("value".equals(reader.getNodeName()))
					System.out.println("found value2" + reader.getValue());
				reader.moveDown();
				if("value".equals(reader.getNodeName()))
					System.out.println("found value3" + reader.getValue());
				reader.moveDown();
				if("value".equals(reader.getNodeName()))
					System.out.println("found value4" + reader.getValue());
			}
		}
		return record;
	}

}
