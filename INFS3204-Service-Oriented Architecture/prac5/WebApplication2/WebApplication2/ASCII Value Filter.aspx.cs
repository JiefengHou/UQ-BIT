using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace WebApplication2
{
    public partial class ASCII_Value_Filter : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        protected void Button1_Click(object sender, EventArgs e)
        {
            int n;
            try
            {
                if (!int.TryParse(TextBox2.Text, out n))
                {
                    throw new Exception("filter should be integer");
                }               
                ServiceReference1.Service1Client ASCIIfilter = new ServiceReference1.Service1Client();
                string result= ASCIIfilter.ASCIIFilter(TextBox1.Text, Convert.ToInt32(TextBox2.Text));
                if (result == "")
                {
                    Label5.Text = "All less than filter!";
                }
                else
                {
                    Label5.Text = result;
                }
            }
            catch (Exception ex)
            {
                Label5.Text = ex.Message;
            }
        }
    }
}