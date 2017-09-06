using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace WebApplication2
{
    public partial class Patient_Registration : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            
        }

        protected void Button1_Click(object sender, EventArgs e)
        {
            Label8.Text = "";
            try
            {
                int n;
                if (string.IsNullOrWhiteSpace(TextBox1.Text) || string.IsNullOrWhiteSpace(TextBox2.Text) || string.IsNullOrWhiteSpace(TextBox3.Text) || string.IsNullOrWhiteSpace(TextBox4.Text) || string.IsNullOrWhiteSpace(TextBox5.Text))
                {
                    throw new Exception("Health Insurance NO.,First name,Last name,Phone number,Address cannot be empty");
                }
                else if (!int.TryParse(TextBox4.Text, out n))
                {
                    throw new Exception("Phone number shoule be an integer");
                }

                LINQDatabaseService linqdbs = new LINQDatabaseService();
                Boolean result = linqdbs.PatientRegistration(TextBox1.Text, TextBox2.Text, TextBox3.Text, Convert.ToInt32(TextBox4.Text), TextBox5.Text, TextBox6.Text);
                
                if (result == true)
                {
                    Label8.Text = "Saved successfully";
                }
            }
            catch (Exception ex)
            {
                Label8.Text = ex.Message;
            }
        }

        protected void Button2_Click(object sender, EventArgs e)
        {
            Label8.Text = "";
            try
            {
                if (string.IsNullOrWhiteSpace(TextBox2.Text) || string.IsNullOrWhiteSpace(TextBox3.Text))
                {
                    throw new Exception("First name,Last name cannot be empty");
                }

                LINQDatabaseService linqdbs = new LINQDatabaseService();
                Patient result = linqdbs.GetPatientInfo(TextBox2.Text, TextBox3.Text);

                TextBox1.Text = result.Health_Insurance_NO_;
                TextBox4.Text = result.Phone_number.ToString();
                TextBox5.Text = result.Address;
                TextBox6.Text = result.Email;
            }
            catch (Exception ex)
            {
                TextBox1.Text = "";
                TextBox4.Text = "";
                TextBox5.Text = "";
                TextBox6.Text = "";  
                Label8.Text = ex.Message;
            }
        }
    }
}