/**
 * 
 */
package ac.analysis.inferenceEngine;

import java.io.IOException;
import java.util.ArrayList;

import ac.analysis.structure.*;



/**
 * La classe qui calcule et stocke les homomorphismes d'un ensemble d'atomes dans un 
 * autre ensemble d'atomes
 *
 */
public class Homomorphisms {
	private ArrayList<Atom> A1;
	private ArrayList<Atom> A2;
	
	private ArrayList<Term> variablesOrdonnees;
	
	private ArrayList<Substitution> S;

	/**
	 * Constructeur de la classe Homomorphismes
	 * @param  ensembleVariables l'ensemble d'atomes � termes variables 
	 * @param ensembleValeurs l'ensemble d'atomes � termes constantes 
	 */
	public Homomorphisms (ArrayList<Atom> ensembleVariables, ArrayList<Atom> ensembleValeurs) 
	{
		A1 = ensembleVariables; //l'ensemble de termes (variables)
		A2 = ensembleValeurs; //l'ensemble de termes (constantes)
		variablesOrdonnees = new ArrayList<Term>();
		S = new ArrayList<Substitution>(); //l'ensemble de homomorphismes initialement vide	
	}
	
	public Homomorphisms(ArrayList<Atom> requete, FactBase bf) 
	{
		A1 = requete; //l'ensemble de termes (variables)
		A2 = bf.getListeAtomes(); //l'ensemble de termes (constantes)
		variablesOrdonnees = new ArrayList<Term>();
		S = new ArrayList<Substitution>();
	}

	public Homomorphisms(Query query, FactBase bf) {
		A1 = query.getListeAtomes();
		A2 = bf.getListeAtomes();
		variablesOrdonnees = new ArrayList<Term>();
		S = new ArrayList<Substitution>();
	}

	/**
	 * M�thode qui retourne les termes de A2
	 */
	private ArrayList<Term> getDomaine()
	{
		ArrayList<Term> terms = new ArrayList<Term>();
		boolean contient;
		for (Atom a: A2)
		{
			for (Term t:a.getListeTermes())
			{
				contient = false;
				for (Term i:terms) {
					if(t.equalsT(i))
						contient = true;
				}
				if(!contient)
					terms.add(t);
			}
		}
		return terms;
	}
	
	/**
	 * M�thode qui retourne les variables de A1
	 */
	private ArrayList<Term> getVariables()
	{
		ArrayList<Term> variables = new ArrayList<Term>();
		
		for (Atom a: A1)
		{
			for (Term t:a.getListeTermes())
			{
				boolean contient=false;
				if (t.isVariable()) 
				{
					for (Term i : variables) 
					{
						if (t.equalsT(i))
							contient = true;
					}
					if (!contient)
						variables.add(t);
				}
			}
		}
		return variables;
	}
	
	/**
	 * M�thode (BacktrackToutesSolutions) qui g�n�re l'ensemble de homomorphismes de A1 dans A2 et le stocke dans S
	 */
	public ArrayList<Substitution> getHomomorphismes() 
	{
		pretraitement();
		backtrackAllRec(new Substitution());
		return S;
	}
	
	/**
	 * M�thode (Backtrack) qui recherche l'existence d'un homomorphisme de A1 dans A2
	 */
	public boolean existeHomomorphisme ()
	{
		pretraitement();
		return backtrackRec(new Substitution ());
	}
	
	/**
	 * M�thode BacktrackRec le sous-algorithme de existeHomomorphisme
	 */
	private boolean backtrackRec(Substitution sol)
	{
		Term x;
		ArrayList<Term> candidats;
		Substitution solPrime;
		if (sol.nombreCouples() == variablesOrdonnees.size())
			return true;
		else
		{
			x = choisirVariableNonAffectee(sol);
			candidats = calculerCandidats(x);
			for (Term c: candidats)
			{
				solPrime = new Substitution (sol);
				solPrime.addCouple(new TermPair(x,c));
				if (estHomomorphismePartiel(solPrime))
					if(backtrackRec(solPrime))
						return true;
			}		
			return false;		
		}
	}
	
	/**
	 * M�thode qui teste si une substitution est un homomorphisme partiel
	 * @param solPrime la substitution � consid�rer
	 */
	private boolean estHomomorphismePartiel(Substitution sol) 
	{
		ArrayList<Atom> A1Prime = new ArrayList<Atom>();
		for (Atom a : A1)
		{
			int counter = 0;
			for (Term t1 : a.getListeTermes())
				for (Term t2 : sol.getVariables())
					if (t1.equalsT(t2)||t1.isConstante())
						counter++;
			if (counter >= a.getListeTermes().size())
				A1Prime.add(a);
		}
		return sol.estHomomorphisme(A1Prime, A2);
	}

	/**
	 * M�thode de pr�traitement des variables de A1 qui calcule un ordre total sur ces variables
	 */
	private void pretraitement()
	{
		//ordonne variables de A1 (donne rang � chacun), ordonne atomes de A1 selon rang		
		variablesOrdonnees = getVariables();
	}
	
	/**
	 * M�thode qui retourne l'ensemble d'images possibles pour la variable donn�e en param�tre
	 * @param x une variable de A1
	 * @return images l'ensemble de termes (constantes) qui sont les images possibles de x
	 */
	private ArrayList<Term> calculerCandidats(Term x)
	{
		return getDomaine();
	}
	
	/**
	 * M�thode qui retourne la prochaine variable non affect�e de A1
	 * 
	 */
	private Term choisirVariableNonAffectee(Substitution sol)
	{
			return variablesOrdonnees.get(sol.nombreCouples());
	}
	
	/**
	 * M�thode BacktrackAllRec le sous-algorithme de getHomomorphismes
	 */
	private void backtrackAllRec(Substitution sol)
	{
		Term x;
		ArrayList<Term> candidats;
		Substitution solPrime;
		if (sol.nombreCouples() == variablesOrdonnees.size())
		{
			S.add(sol);
			return;
		}
		else
		{
			x = choisirVariableNonAffectee(sol);
			candidats = calculerCandidats(x);
			for (Term c: candidats)
			{
				solPrime = new Substitution (sol);
				solPrime.addCouple(new TermPair(x,c));
				if (estHomomorphismePartiel(solPrime))
					backtrackAllRec(solPrime);
			}		
		}
	}

	public static void main(String[] args) throws IOException
	{
		KnowledgeBase k = new KnowledgeBase("ex2.txt");
		k = k.saturationOrdre1Exploite();
		Query query = new Query("p('A',y);r(x,y,z)");
		Homomorphisms h = new Homomorphisms (query,k.getBF());
		System.out.println("A1 = " + query + "\nA2 = " + k.getBF() + "\n\nExistent-ils d'homomorphismes de A1 dans A2 ?");
		if(h.existeHomomorphisme())
		{
			System.out.println(" Oui");
			System.out.println("La liste de r�ponses est :");
			System.out.println(h.getHomomorphismes());
		}
		else
			System.out.println("Non");
	}
}
