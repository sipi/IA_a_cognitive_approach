package structure;

/**
 * Le couple de termes qui d�finit la substitution d'une variable par une constante.
 * C'est l'�l�ment de base pour la classe Substitution.
 */
public class CoupleTermes 
{
	
	private Terme x; //1e terme du couple (variable)
	private Terme y; //2e terme du couple (constante)

	/**
	 * Constructeur de la classe CoupleTermes
	 * @param a premier terme du couple
	 * @param b deuxi�me terme du couple
	 */
	public CoupleTermes(Terme a, Terme b) 
	{
		x = a;
		y = b;
	}
	
//Les getters de la classe
	public Terme getX()
	{
		return x;
	}
	
	public Terme getY()
	{
		return y;
	}
	
//Les m�thodes qui caract�risent les fonctionnalit�es de la classe
	/**
	 * m�thode qui remplace x (le 1e terme du couple) par y (le 2e terme du couple) dans une string
	 * @param s string dans laquelle on d�sire faire le remplacement
	 * @return t nouvelle string avec x remplac� par y
	 */
	public String replaceXY (String s)
	{
		String t = s.replaceAll(x.toString(), y.toString());
		return t;
	}
//M�thode toString de la classe	
	public String toString()
	{
		String s = "(" + x + "," + y + ")";
	return s;
	}

}
