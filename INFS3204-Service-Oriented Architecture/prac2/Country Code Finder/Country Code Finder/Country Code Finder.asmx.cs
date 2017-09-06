using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;
using System.Text.RegularExpressions;

namespace Country_Code_Finder
{
    /// <summary>
    /// Summary description for Country_Code_Finder1
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
    [System.Web.Script.Services.ScriptService]
    public class Country_Code_Finder1 : System.Web.Services.WebService
    {

        [WebMethod]
        public string dispalyTime()
        {
            return DateTime.Now.TimeOfDay.ToString();
                
        }

        [WebMethod]
        public string findCountryCode(string input)
        {
            string path = System.AppDomain.CurrentDomain.BaseDirectory.ToString();

            System.IO.FileStream fs = new System.IO.FileStream(path + "countryCode.txt", System.IO.FileMode.Open, System.IO.FileAccess.Read);
            System.IO.StreamReader sr = new System.IO.StreamReader(fs, System.Text.Encoding.Default);

            string[] txt = sr.ReadToEnd().Split('#');
            sr.Close();

            string result = "";
            int num = 0;

            Regex r = new Regex(@"^" + input, RegexOptions.IgnoreCase);

            for (int i = 0; i < txt.Length; i++)
            {
                if (r.IsMatch(txt[i]))
                {
                    result += txt[i] + " ";
                    num++;
                }
            }

            if (num == 0)
            {
                return "No results found for yout input!";
            }
            else
            {
                return result;
            }
            sr.Close();
        }
    }
}
