import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main 
{
	public static void main(String args[]) throws IOException
	{
		BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("暗号化：0, 複号化：1");
		String str = br.readLine();
		int inv = Integer.parseInt(str);
		
		System.out.println("入力：");
		str = br.readLine();
		int input = Integer.parseInt(str);
		
		System.out.println("鍵：");
		str = br.readLine();
		int key = Integer.parseInt(str);
		
		int output = des(input, key, inv);
		
		System.out.println("出力："+ output);
		
		System.out.println();
	}
	
	public static int P10(int i)
	{
		return ((i)<<2&0x200)|((i)<<3&0x100)|((i)>>1&0x80)|((i)<<3&0x40)|((i)>>1&0x20)|((i)<<4&0x10)|((i)>>6&0x08)|((i)<<1&0x04)|((i)>>1&0x02)|((i)>>4&0x01);
	}
	
	public static int P8(int i)
	{
		return ((i)<<3&0x80)|((i)<<1&0x40)|((i)<<2&0x20)|((i)>>2&0x10)|((i)<<1&0x08)|((i)>>3&0x04)|((i)<<1&0x02)|((i)>>1&0x01);
	}
	
	public static int P4(int i)
	{
		return ((i)<<1&0x08)|((i)<<2&0x04)|((i)&0x02)|((i)>>3&0x01);
	}
	
	public static int LS1(int i)
	{
		return ((((i)<<1)|((i)>>4&1))&0x1F);
	}
	
	public static int LS2(int i)
	{
		return ((((i)<<2)|((i)>>3&3))&0x1F);
	}
	
	public static int IP(int i)
	{
		return ((i)<<1&0x80)|((i)<<4&0x40)|((i)&0x20)|((i)>>3&0x10)|((i)>>1&0x08)|((i)<<2&0x04)|((i)>>2&0x02)|((i)>>1&0x01);
	}
	
	public static int IP_1(int i)
	{
		return ((i)<<3&0x80)|((i)>>1&0x40)|((i)&0x20)|((i)<<1&0x10)|((i)<<2&0x08)|((i)>>4&0x04)|((i)<<1&0x02)|((i)>>2&0x01);
	}
	
	public static int EP(int i)
	{
		return ((i)<<7&0x80)|((i)<<3&0x40)|((i)<<3&0x20)|((i)<<3&0x10)|((i)<<1&0x08)|((i)<<1&0x04)|((i)<<1&0x02)|((i)>>3&0x01);
	}
	
	public static int SW(int i)
	{
		return (((i)<<4)|((i)>>4));
	}
	
	public static int des(int input, int key, int inv)
	{
		int key1, key2;
		int temp1, temp2, temp;
		
		int[][] s0 = 
			{
				{1,0,3,2},
				{3,2,1,0},
				{0,2,1,3},
				{3,1,3,2}
			};
		
		int[][] s1 = 
			{
				{0,1,2,3},
				{2,0,1,3},
				{3,0,1,0},
				{2,1,0,3}
			};
		
		key = P10(key);
		key = LS1(key>>5)<<5|LS1(key&0x1F);
		key1 = P8(key);
		key = LS2(key>>5)<<5|LS2(key&0x1F);
		key2 = P8(key);
		
		if(inv == 1)
		{
			temp1 = key1;
			key1 = key2;
			key2 = temp1;
		}
		
		temp2 = IP(input);
		
		// round 
		temp = EP(temp2&0xF) ^ key1;
		temp = s0[(temp>>6&0x2)|(temp>>4&0x1)][(temp>>5&0x3)]<<2 | s1[(temp>>2&0x2)|(temp&0x1)][(temp>>1&0x3)];
		temp = P4(temp) << 4 ^ temp2;
						
		temp2= SW(temp)&0xFF;

		// round 2
		temp = EP(temp2&0xF) ^ key2;
		temp = s0[(temp>>6&0x2)|(temp>>4&0x1)][(temp>>5&0x3)]<<2 | s1[(temp>>2&0x2)|(temp&0x1)][(temp>>1&0x3)];
		temp = P4(temp) << 4 ^ temp2;

	    return IP_1(temp);

		
	} 
}


