/**
 * 
 */
package es.um.multigraph.decision.lwang;

import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 
 * A very inefficient solution to store a logical proposition using Lists.
 * Represents the result of the NetworkHardening algorithm in L.Wang et al. aka
 * the logical proposition L =((c1 AND c2) OR (c2 AND c3)) to be negated. the
 * AND OR logical operators are interpreted like this: a list of only
 * MyNode.EXPLOITSs has OR for every comma; a list of only MyNode.CONDITIONs has
 * AND for every comma; the innermost elements are always conditions, thus they
 * will be all ANDed within their respective lists, this means the imemdiate
 * parent level (i.e. the parent list) will be all ORed because it contained
 * EXPLOIT type elements, and so on. TODO what if we want to flatten L?
 * 
 * @author Pavlo Burda - p.burda@tue.nl
 *
 */
public class InitialConds {

	List<Object> L;
	MyNode c1;
	MyNode c2;
	MyNode c3;
	MyNode c4;

	public InitialConds() {
		this.L = new LinkedList<Object>();
		this.c1 = new MyNode("c1");
		this.c2 = new MyNode("c2");
		this.c3 = new MyNode("c3");
		this.c4 = new MyNode("c4");
	}

	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void listTest() {

		this.L = new LinkedList<Object>();

		this.L.add(new LinkedList<>());
		List tmp1 = (LinkedList) this.L.get(0);
		tmp1.add(c1);
		List tmp2 = (LinkedList) this.L.get(0);
		tmp2.add(c2);
		this.L.add(new LinkedList<>());
		List tmp3 = (LinkedList) this.L.get(1);
		tmp3.add(c3);
		List tmp4 = (LinkedList) this.L.get(1);
		tmp4.add(c4);

		assertNotNull(L);
		assertEquals(L.toString(),
				"[[ID: c1 - Label: null, ID: c2 - Label: null], [ID: c3 - Label: null, ID: c4 - Label: null]]");

	}

	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void test_Replace() {

		this.L = new LinkedList<Object>();

		this.L.add(new LinkedList<>());
		List tmp1 = (LinkedList) this.L.get(0);
		tmp1.add(c1);
		List tmp2 = (LinkedList) this.L.get(0);
		tmp2.add(c2);
		this.L.add(new LinkedList<>());
		List tmp3 = (LinkedList) this.L.get(1);
		tmp3.add(c3);
		List tmp4 = (LinkedList) this.L.get(1);
		tmp4.add(c4);

		List<Object> newEl = new LinkedList<>();
		MyNode e1 = new MyNode("e1");
		newEl.add(e1);

		System.out.println("Before replace: " + L);
		replaceElpriv(newEl, this.c4, this.L);

		System.out.println("Replace: " + L);

		assertNotNull(newEl);
		assertEquals(L.toString(),
				"[[ID: c1 - Label: null, ID: c2 - Label: null], [ID: c3 - Label: null, [ID: e1 - Label: null]]]");

	}

	@Test
	public void flattenTest() {

		this.L = new LinkedList<Object>();

		List<Object> upperList = new LinkedList<>();
		List<Object> middleList = new LinkedList<>();
		List<Object> newEl = new LinkedList<>();
		MyNode c1 = new MyNode("c1");
		newEl.add(c1);
		MyNode c2 = new MyNode("c2");
		newEl.add(c2);
		middleList.add(newEl);
		upperList.add(middleList);
		this.L.add(upperList);

		System.out.println("Before flatten: " + this.L);
		flattenPriv(this.L);
		System.out.println("Flatten: " + this.L);

		assertNotNull(this.L);
		assertEquals(this.L.toString(), "[ID: c1 - Label: null, ID: c2 - Label: null]");

	}

	/**
	 * We have LinkedList<MyNode> L = {c} iterate over L and locate the element c it
	 * has to become a list of sub-elements, thus we need to declare a new type for
	 * L: LinkedList<LinkedList<MyNode>> L = {LinkedList<MyNodes>={e1,e2}}; but now
	 * we have to locate e1 and e2 (not knowing how deep they are) and substitute
	 * them with c1,c2 and c3,c4...
	 * 
	 * @param list  is the L list that contains the final result
	 * @param newEl is the new element
	 * @param oldEl is the element to be replaced
	 * @return true if success
	 * 
	 */
	private boolean replaceElpriv(List<Object> newEl, MyNode oldEl, List<Object> list) {
		if (list == null)
			return false;

		int index = list.indexOf(oldEl);
		boolean replaced = false;

		if (index >= 0) {
			list.set(index, newEl);
			return true;
		} else {
			for (Object obj : list) {
				if (!obj.getClass().equals(MyNode.class))
					replaced = replaceElpriv(newEl, oldEl, (List<Object>) obj);
			}
		}
		return replaced;
	}

	/**
	 * Replace a node with its decomposition as in L.Wang et al.
	 * 
	 * @param list  is the L list that contains the final result
	 * @param newEl is the new element
	 * @param oldEl is the element to be replaced
	 * @return L the intermediate result
	 */
	public List<Object> replaceEl(List<Object> newEl, MyNode oldEl, List<Object> list) {

		this.L = list;
		replaceElpriv(newEl, oldEl, list);

		return this.L;
	}

	private boolean flattenPriv(List<Object> list) {

		if (list.size() == 0) {
			return false;
		}

		boolean flag = false;

		if (list.size() > 1)
			this.L = list;
		else
			flag = flattenPriv((List<Object>) list.get(0));

		return flag;

	}

	/**
	 * Replace a node with its decomposition as in L.Wang et al.
	 * 
	 * @param list is the L list that contains the final result in case it has too
	 *             many sublists
	 * @return L the flattened list
	 */
	public List<Object> flatten(List<Object> list) {
		this.L = list;
		flattenPriv(list);

		return this.L;
	}

}
