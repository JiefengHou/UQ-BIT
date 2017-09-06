using System;
using System.Collections;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace WebApplication2
{
    public partial class WebForm1 : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        protected void Button1_Click(object sender, EventArgs e)
        {
            try
            {
                int n;
                ServiceReference1.Service1Client Anagrams = new ServiceReference1.Service1Client();
                string[] words = TextBox1.Text.Split(',');
                foreach (string word in words)
                {
                    if (int.TryParse(word, out n))
                    {
                        throw new Exception("input cannot contains integer");
                    }
                }
                string[][] result = Anagrams.AnagramsFinder(words);
                string output="";
                foreach (string[] group in result)
                {
                    string groups = string.Join(",", group);
                    if (output == "")
                    {
                        output = "["+groups+"]";
                    }
                    else
                    {
                        output = output + "," + "[" + groups + "]";
                    }
                   
                }
                Label3.Text = "["+output+"]";

            }
            catch(Exception ex)
            {
                Label3.Text = ex.Message;
            }

            
        }
    }
}