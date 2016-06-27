package storage;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import misc.condition.Condition;
import parser.StringHeirachy;

public abstract class Storer <StoreType extends Storable>{
	protected Object key;
	protected StoreType self;
	protected String identifier;
	protected int integers=0;
	protected int booleans=0;
	protected int floats = 0;
	protected int strings = 0;
	public Storer(StoreType self, String id){
		this.self = self;
		this.identifier = id;
	}
	protected abstract Object[] storeCharacteristics();
	public void store(List<Byte> build, Map<String,Integer> strings){
		encode(strings,build);
		for(Storer storer:self.getStorerIterator()){
			storer.store(build,strings);
		}
	}

	protected abstract void adjust(Object... args);
	public int load(Byte[] cs,int startAt,Map<Integer,String> strings){
		//System.out.println("load:"+this.getClass().getName());
		//System.out.println("startAt:"+startAt);
		List<Object> objs = new ArrayList<Object>();
		for(int index = 0;index<integers;++index){
			Integer[] result = decodeInteger(cs,startAt);
			objs.add(result[0]);
			startAt=result[1];
		}
		for(int index=0;index<floats;++index){
			objs.add(decodeFloat(cs,startAt));
			startAt+=4;
		}
		for(int index=0;index<this.strings;++index){
			Integer[] result = decodeInteger(cs,startAt);
			objs.add(strings.get(result[0]));
			startAt=result[1];
		}
		Boolean[] bools = decodeBooleans(cs, booleans, startAt);
		for(int i=0;i<bools.length;++i){
			objs.add(bools[i]);
			if(i%8==0){
				++startAt;
			}
		}
		adjust(objs.toArray());
		for(Storer storer:self.getStorerIterator()){
			if(startAt<cs.length){
				startAt = storer.load(cs,startAt,strings);
			}
			else break;
		}
		return startAt;
	}


	protected void encode(Map<String,Integer> strings, List<Byte> build){
		Object[] characteristics = this.storeCharacteristics();
		for(int i=0;i<integers;++i){
			encodeInteger((Integer) characteristics[i],build);
		}
		if(floats>0){
			for(int i=0;i<floats;++i){
				encodeFloat((Float)characteristics[i],build);
			}
		}
		if(this.strings>0){
			for(int i=0;i<this.strings;++i){
				encodeString(characteristics[i+integers+floats].toString(),strings,build);
			}
		}
		if(booleans>0){
			Boolean[] bools = new Boolean[booleans];
			for(int i=0;i<booleans;++i){
				bools[i] = (Boolean) characteristics[i+integers+floats+this.strings];
			}
			encodeBooleans(bools,build);
		}
	}

	public static void encodeInteger(Integer i, List<Byte> build){
		String ret = "";
		int c = 0;
		int f = 1;
		int mod = 64;
		int negmod = 64;
		if(i<0){
			negmod = 0;
			i=-i;
		}
		List<Byte> temp = new ArrayList<Byte>();
		do{
			c=(i/f)%mod;
			temp.add(0,(byte) ((char)(mod==64?(int)(c-128+negmod):c)));
			f*=mod;
			mod=128;
		} while(i>f);
		build.addAll(temp);
	}

	public static void encodeFloat(Float f, List<Byte> build){
		byte[] bytes = ByteBuffer.allocate(4).putFloat(f).array();
		for(int i=0;i<4;++i){
			build.add(bytes[i]);
		}

	}

	public static void encodeString(String string, Map<String,Integer> strings, List<Byte> build){
		if(!strings.containsKey(string)){
			strings.put(string, strings.size());
		}
		encodeInteger(strings.get(string),build);
	}

	public static void encodeBooleans(Boolean[] bools,List<Byte> build){
		Byte[] bytes = new Byte[bools.length/7+1];
		for(int i=0;i<bytes.length;++i){
			bytes[i]=0;
		}
		for(int arrayIndex = 0, boolIndex = 0, factor=1;boolIndex<bools.length;++boolIndex){
			bytes[arrayIndex] = (byte) (bytes[arrayIndex] + (bools[boolIndex]?(byte)factor:(byte)0));
			if(boolIndex>0&&boolIndex%6==0){
				++arrayIndex;
				factor = 1;
			}
			else {
				factor*=2;
			}
		}
		build.addAll(Arrays.asList(bytes));
	}

	public static Float decodeFloat(Byte[] cs, int a){
		byte[] b = new byte[4];
		for(int i=0;i<4;++i){
			b[i]=cs[i+a];
		}
		return  ByteBuffer.wrap(b).getFloat();
	}


	public static Integer[] decodeInteger(Byte[] cs, int a){
		int i=0;
		while(cs[a]>=0){
			i*=128;
			i+=cs[a];
			++a;
		}
		i*=64;
		if(cs[a]>=-128+64){
			i+=cs[a]-64+128;			
		}
		else {
			i+=cs[a]+128;
			i=-i;
		}
		return new Integer[]{i,a+1};
	}
	public static Boolean[] decodeBooleans(Byte[] cs, int numberOf, int startAt){
		Boolean[] bools = new Boolean[numberOf];
		int index = 0;
		while(index<numberOf){
			int current = cs[startAt++];
			int factor = 2;
			while(factor<256&&index<numberOf){
				bools[index++]=current%factor!=0;
				current-=current%factor;
				factor*=2;
			}
		}
		return bools;
	}
	protected Object[] o(Object...objects){
		return objects;
	}
	public StoreType getSelf() {
		return self;
	}
}
