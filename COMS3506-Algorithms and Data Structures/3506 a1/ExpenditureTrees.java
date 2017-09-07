package part2;

import adt.*;

import java.util.*;

public class ExpenditureTrees {

	/**
	 * This method takes a tree describing a hierarchical classification scheme
	 * of expenditures, and a list of expenditure types from that tree and
	 * returns the <b>summary</b> of that list of types (as described in the
	 * assignment handout). The summary is calculated with respect to the given
	 * tree.
	 * 
	 * The expenditure types in the tree and the list are denoted by non-null
	 * Strings. Two types are the same if their String representations are equal
	 * (using the ".equals()" method). The same type may not occur in two
	 * different positions in the tree. The same type may not appear twice in
	 * the given list of types either. Each type in the list should be equal to
	 * one in the tree.
	 * 
	 * This method assumes that the parameters tree and types are non-null and
	 * that the tree is non-empty. The list of types may be an empty list.
	 * 
	 * The <b>summary</b> of the list of types is returned by the method as a
	 * non-null list of expenditure types. The returned list should not contain
	 * duplicate types (since it denotes a set).
	 * 
	 * This method should not modify its parameters in any way.
	 */
	public static List<String> summary(Tree<String> tree, List<String> types) {
		return types.size()==0?new ArrayList<String>():
			iterator(tree.root(),tree,types);
	}
	
	/*
	 * return a list of string about summary types
	 */
	private static List<String> iterator(Position<String> node,Tree<String> tree
			,List<String> types) {
		List<String> summary = new ArrayList<String>();
		if(node != null) {
			if(!types.contains(node.getElement())&&tree.numChildren(node)==0) {
				return summary;
			} else if(types.contains(node.getElement())) {
				summary.add(node.getElement());
			} else {
				for (Position<String> childNode : tree.children(node)) {
					List<String> childrenSummary = iterator(childNode,tree,types);
					if(childrenSummary.size() == tree.numChildren(childNode) && 
							tree.numChildren(childNode)!=0 && checkChildren(
									tree.children(childNode),childrenSummary)){
						summary.add(childNode.getElement());
					} else summary.addAll(childrenSummary);	
				}
			}
			if(tree.root()==node) {
				if(tree.numChildren(node)==summary.size()&&
						checkChildren(tree.children(node),summary)){
					summary.clear();
					summary.add(node.getElement());						
				}
			}
		}
		return summary;
	}
	
	/*
	 * return a boolean, if all children values of the node are equal values in
	 * summary list, return true. Otherwise, return false. 
	 */
	private static boolean checkChildren(List<Position<String>> children,
			List<String> summary) {
		boolean equal = true;
		int childNumber = children.size();
		for(int i=0;i<childNumber;i++) {
			if(!children.get(i).getElement().equals(summary.get(i))) {
				equal = false;
			}
		}
		return equal;
	}

	/**
	 * This method takes a tree describing a hierarchical classification scheme
	 * of expenditures, a <b>concise</b> list of expenditure types from that
	 * tree, and an expenditure type from the tree and returns true if the given
	 * expenditure type is in the <b>closure</b> of the concise list of types,
	 * and false otherwise. (See the assignment handout for definitions.)
	 * 
	 * The expenditure types in the tree and the list are denoted by non-null
	 * Strings. Two types are the same if their String representations are equal
	 * (using the ".equals()" method). The same type may not occur in two
	 * different positions in the tree. The same type may not appear twice in
	 * the given list of types either. Each type in the list should be equal to
	 * one in the tree. The given expenseType should also be equal to one in the
	 * tree.
	 * 
	 * This method assumes that the parameters tree, types and expenseType are
	 * non-null and that the tree is non-empty. The list of types may be an
	 * empty list.
	 * 
	 * This method should not modify its parameters in any way.
	 */
	public static boolean contains(Tree<String> tree, List<String> types,
			String expenseType) {
		if(types.size()==0) return false; 
		else return iterator2(tree.root(),tree,types,false).contains(expenseType)?
				true:false;
	}
	
	/*
	 * return a list of node and its all children
	 */
	public static List<String> iterator2(Position<String> node,Tree<String> tree
			,List<String> types,boolean contain) {
		List<String> summary = new ArrayList<String>();
		if(node != null) {
			if(types.contains(node.getElement()) || contain == true) {
				summary.add(node.getElement());
				contain = true;
			} 
			int childNumber = tree.numChildren(node);
			for(int i=0;i<childNumber;i++) {
				Position<String> childNode = tree.children(node).get(i);
				if(tree.numChildren(childNode)==0 && i==childNumber) {
					contain = false;
				}
				summary.addAll(iterator2(childNode,tree,types,contain));
			}
		}	
		return summary;
	}
	
	/**
	 * This method takes a tree describing a hierarchical classification scheme
	 * of expenditures, and a list of expenditure types from that tree and
	 * returns the <b>negation</b> of that list of types (as described in the
	 * assignment handout). The negation is calculated with respect to the given
	 * tree.
	 * 
	 * The expenditure types in the tree and the list are denoted by non-null
	 * Strings. Two types are the same if their String representations are equal
	 * (using the ".equals()" method). The same type may not occur in two
	 * different positions in the tree. The same type may not appear twice in
	 * the given list of types either. Each type in the list should be equal to
	 * one in the tree.
	 * 
	 * This method assumes that the parameters tree and types are non-null and
	 * that the tree is non-empty. The list of types may be an empty list.
	 * 
	 * The <b>negation</b> of the list of types is returned by the method as a
	 * non-null list of expenditure types. The returned list should not contain
	 * duplicate types (since it denotes a set).
	 * 
	 * This method should not modify its parameters in any way.
	 */
	public static List<String> negation(Tree<String> tree, List<String> types) {
		return types.size()==0?new ArrayList<String>():iterator3(tree.root(),tree,types);
	}
	
	/*
	 * return a list of negation of contain type
	 */
	private static List<String> iterator3(Position<String> node,Tree<String> tree
			,List<String> types) {
		List<String> summary = new ArrayList<String>();
		if(node != null) {
			if(types.contains(node.getElement())) {
				return summary;
			} else {	
				for (Position<String> childNode : tree.children(node)) {
					List<String> childrenSummary = iterator3(childNode,tree,types);
					if(!types.contains(childNode.getElement())&&
							tree.numChildren(childNode)==0) {
						summary.add(childNode.getElement());
					} else if(childrenSummary.size()==tree.numChildren(childNode)
							&& tree.numChildren(childNode)!=0 &&
									checkChildren(tree.children(childNode),
											childrenSummary)) {
						summary.add(childNode.getElement());
					} else summary.addAll(childrenSummary);
				}
			}
		}
		return summary;
	}
}
