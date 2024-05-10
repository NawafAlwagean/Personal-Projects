import java.io.*;
import java.util.Scanner;

class VisitorInfo {
	String LName, FName;
	int type;
	String Phone_number;
	int region;
	ArrayStack<Integer> order = new ArrayStack<Integer>(4);

	public VisitorInfo(String FName, String LName, int Region, int pass_Type, String phone_number) {

		FName = FName;
		LName = LName;
		this.type = pass_Type;
		Phone_number = phone_number;
		this.region = region;
	}

	public void display() {
		System.out.println("Name: " + FName + " " + LName);
		System.out.println("Region: " + region);
		if (type == 0)
			System.out.println("VIP Pass_Holder : NO");
		else if (type == 1)
			System.out.println("VIP Pass_Holder : YES");
		System.out.println("Phone_number: " + Phone_number);
		System.out.println("Order of visiting the kingdoms:");
		displayOrders(order);
		System.out.println("");
	}

	public void displayOrders(ArrayStack<Integer> s1) {
		ArrayStack<Integer> temp = new ArrayStack<Integer>(4);
		while (!s1.empty()) {
			temp.push(s1.pop());
		}
		while (!temp.empty()) {
			Integer x = temp.pop();
			s1.push(x);
			if (!temp.empty())
				System.out.println(x + ",");
			else
				System.out.println(x);
		}

	}
}

class visitorType {
	int type;
	int num_Visitors;
	LinkedList<VisitorInfo> visitList;

	public visitorType(int type, int num_visitors) {
		this.type = type;
		this.num_Visitors = num_visitors;
		visitList = new LinkedList<VisitorInfo>();

	}

	public visitorType() {
		// TODO Auto-generated constructor stub
	}
}

class rgnInfo {
	public static int total_visitor;
	int region, total_visitors;
	visitorType[] Vtype = new visitorType[2];

	public rgnInfo(int region, int total_visitors) {
		this.region = region;
		this.total_visitors = total_visitors;
		Vtype[0] = new visitorType(0, 0);
		Vtype[1] = new visitorType(1, 0);
	}

	public rgnInfo() {
		// TODO Auto-generated constructor stub
	}
}

public class ThemeParkADT implements Serializable {
	private rgnInfo rgnSortedArray[];
	private rgnInfo regionArray[];
	int max_region = 0;
	int nb_new_region = 0;
	LinkedList<VisitorInfo> visitors;
	LinkedList<VisitorInfo> vips;

	public ThemeParkADT() {
		visitors = new LinkedList<VisitorInfo>();
		vips = new LinkedList<VisitorInfo>();
	}

	public void readFileAndAnalyse(String file) throws IOException {
		File f = new File(file);
		FileInputStream inStream = new FileInputStream(f);
		ObjectInputStream inDon = new ObjectInputStream(inStream);
		int max = 0;
		int count = 0;
		visitors = new LinkedList<VisitorInfo>();
		vips = new LinkedList<VisitorInfo>();
		LinkedList<VisitorInfo> regions = new LinkedList<>();
		try {
			while (true) {
				VisitorInfo v = (VisitorInfo) inDon.readObject();
				visitors.insert(v);
				if (v.type == 1)
					vips.insert(v);
				if (v.region > max)
					max = v.region;
				if (count == 0) {
					regions.insert(v);
					count++;
				} else if (count > 0) {
					boolean flag = true;
					regions.findfirst();
					while (regions.current != null) {
						if (v.region == regions.current.data.region)
							flag = false;
						regions.current = regions.current.next;
					}
					if (flag) {
						regions.findfirst();
						regions.insert(v);
						count++;
					}
				}

			}
		} catch (EOFException e1) {
			System.out.println("Reading is completed");
			inStream.close();
			inDon.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		visitors.findfirst();
		rgnSortedArray = new rgnInfo[count];
		regionArray = new rgnInfo[max + 1];
		regions.findfirst();
		for (int i = 0; i < count; i++) {
			int v = 0, r = 0;
			LinkedList<VisitorInfo> l1 = new LinkedList<VisitorInfo>();
			LinkedList<VisitorInfo> l2 = new LinkedList<VisitorInfo>();
			visitorType vt[] = new visitorType[2];
			visitorType v1 = new visitorType();
			visitorType v2 = new visitorType();
			rgnInfo s = new rgnInfo();
			rgnInfo s1 = new rgnInfo();
			visitors.findfirst();
			int d = regions.current.data.region;
			while (visitors.current != null) {
				if (visitors.current.data.region == d & visitors.current.data.type == 1) {
					l1.insert(visitors.current.data);
					v++;
				} else if (visitors.current.data.region == d & visitors.current.data.type == 0) {
					l2.insert(visitors.current.data);
					r++;
				}
				visitors.current = visitors.current.next;

			}
			v1.visitList = l1;
			v2.visitList = l2;
			v1.num_Visitors = v;
			v2.num_Visitors = r;
			vt[0] = v2;
			vt[1] = v1;
			s.Vtype = vt;
			s.total_visitor = r + v;
			s.region = d;
			s1.region = d;
			s1.total_visitor = s.total_visitor;
			regionArray[d] = s;
			rgnSortedArray[i] = s1;
			regions.findfirst();
		}

		bubbleSort(rgnSortedArray, count);
	}

	private static void bubbleSort(rgnInfo A[], int n) {
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - 1 - i; j++) {
				if (A[j].total_visitors < A[j + 1].total_visitor) {
					rgnInfo tmp = A[j];
					A[j] = A[j + 1];
					A[j + 1] = tmp;
				}
			}
		}
	}

	public void searchVisitor(String LName) {
		if (visitors.empty()) {
			System.out.println("empty");
			System.out.println("there is not any visitor with the last name:" + LName);
		} else {
			boolean found = false;
			int i = 1;
			visitors.findfirst();
			while (!visitors.last()) {
				if (visitors.retrieve().LName.equals(LName)) {
					System.out.println("visitor " + i + ": ");
					visitors.retrieve().display();
					i++;
					found = true;
				}
				visitors.findnext();

			}
			if (visitors.retrieve().LName.equals(LName)) {
				System.out.println("Visitors " + i + ": ");
				visitors.retrieve().display();
				i++;
				found = true;

			}
			if (!found)
				System.out.println("there is not any visitors with the last name:" + LName);

		}
	}

	public void rgnCount() {
		System.out.println("The total number of regions are" + nb_new_region);
	}

	public void poplarRgn() {
		for (int i = 1; i < nb_new_region; i++)
			System.out.println("region=" + rgnSortedArray[i].region + ":" + rgnSortedArray[i].total_visitors);

	}

	public void vipRgn(int i) {
		System.out.println("The total number of VIP pass holders coming from Region " + i + " is "
				+ regionArray[i].Vtype[1].num_Visitors);
	}

	public void vipLocation() {
		if (vips.empty()) {
			System.out.println("there is not any vips visitors");
		} else {
			vips.findfirst();
			while (!vips.last()) {
				int curLoc = vips.retrieve().order.pop();
				System.out.println(vips.retrieve().FName + " " + vips.retrieve().LName + " in kingdom " + curLoc);
				vips.retrieve().order.push(curLoc);

				vips.findnext();

			}
			int curLoc = vips.retrieve().order.pop();
			System.out.println(vips.retrieve().FName + " " + vips.retrieve().LName + " in kingdom " + curLoc);
			vips.retrieve().order.push(curLoc);

		}
	}

	public boolean checkVipLoc(String n1, String n2) {
		if (vips.empty()) {
			System.out.println("there is not any vip visitors");
			return false;

		} else {
			boolean isVIP1 = false;
			boolean isVIP2 = false;
			int loc1 = -1, loc2 = -2;
			int reg1 = -1, reg2 = -2;
			vips.findfirst();
			while (!vips.last()) {
				int curLoc = vips.retrieve().order.pop();
				vips.retrieve().order.push(curLoc);

				if (vips.retrieve().Phone_number.equals(n1)) {
					loc1 = curLoc;
					reg1 = vips.retrieve().region;
					isVIP1 = true;

				}
				if (vips.retrieve().Phone_number.equals(n2)) {
					loc2 = curLoc;
					reg2 = vips.retrieve().region;
					isVIP2 = true;
				}
				vips.findnext();
			}
			int curLoc = vips.retrieve().order.pop();
			vips.retrieve().order.push(curLoc);
			if (vips.retrieve().Phone_number.equals(n1)) {
				loc1 = curLoc;
				reg1 = vips.retrieve().region;
				isVIP1 = true;

			}
			if (vips.retrieve().Phone_number.equals(n2)) {
				loc2 = curLoc;
				reg2 = vips.retrieve().region;
				isVIP2 = true;
			}
			if (!isVIP1) {
				System.out.println(n1 + "is not vip or not exist");
				return false;
			}
			if (!isVIP2) {
				System.out.println(n1 + "is not vip or not exist");
				return false;
			}
			if (reg1 != reg2) {
				System.out.println("there are not from the same region");
				return false;

			}
			if (loc1 != loc2) {
				System.out.println("there are not in the same kimgdom");
				return false;
			}
			return true;

		}

	}

	private boolean checkEqOrders(ArrayStack<Integer> s1, ArrayStack<Integer> s2) {
		boolean equal = true;
		ArrayStack<Integer> temp1 = new ArrayStack<Integer>(4);
		ArrayStack<Integer> temp2 = new ArrayStack<Integer>(4);
		while (!s1.empty() && !s2.empty()) {
			Integer x1 = s1.pop();
			temp1.push(x1);
			Integer x2 = s2.pop();
			temp2.push(x2);
			if (x1 != x2)
				equal = false;

		}
		if (s1.empty() && !s2.empty() || !s1.empty() && s2.empty())
			equal = false;
		while (!temp1.empty())
			s1.push(temp1.pop());
		while (!temp2.empty())
			s2.push(temp2.pop());
		return equal;
	}

	public boolean checkRegLoc(int r, String n1, String n2) {
		LinkedList<VisitorInfo> D = regionArray[r].Vtype[0].visitList;
		ArrayStack<Integer> ro1 = new ArrayStack<Integer>(4);
		ArrayStack<Integer> ro2 = new ArrayStack<Integer>(4);
		if (D.empty()) {
			System.out.println("there is not any regular visitor from region" + r);
			return false;

		} else {
			D.findfirst();
			while (!D.last()) {
				if (D.retrieve().Phone_number.equals(n1))
					ro1 = D.retrieve().order;
				if (D.retrieve().Phone_number.equals(n2))
					ro2 = D.retrieve().order;
				D.findnext();

			}
			if (D.retrieve().Phone_number.equals(n1))
				ro1 = D.retrieve().order;
			if (D.retrieve().Phone_number.equals(n2))
				ro2 = D.retrieve().order;

			if (ro1.empty()) {
				System.out.println("visitor with phone " + n1 + " not exist");
				return false;
			}
			if (ro2.empty()) {
				System.out.println("visitor with phone " + n2 + " not exist");
				return false;
			}
			if (checkEqOrders(ro1, ro2))
				return true;
			else {
				System.out.println("the order of kingdoms not the same");
				return false;
			}

		}
	}
}
