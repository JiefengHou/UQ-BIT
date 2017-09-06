using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace WebApplication2
{
    public partial class Doctor_Registration : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        protected void Button1_Click(object sender, EventArgs e)
        {
            Label7.Text = "";
            try
            {

                int n;
                if (string.IsNullOrWhiteSpace(TextBox1.Text) || string.IsNullOrWhiteSpace(TextBox2.Text) || string.IsNullOrWhiteSpace(TextBox3.Text) || string.IsNullOrWhiteSpace(DropDownList1.SelectedValue) || string.IsNullOrWhiteSpace(TextBox4.Text))
                {
                    throw new Exception("Health Insurance NO.,First name,Last name,Health Profession,Phone number cannot be empty");
                }
                else if (!int.TryParse(TextBox4.Text, out n))
                {
                    throw new Exception("Phone number shoule be an integer");
                }

                LINQDatabaseService linqdbs = new LINQDatabaseService();
                Boolean result = linqdbs.DoctorRegistration(TextBox1.Text, TextBox2.Text, TextBox3.Text, DropDownList1.SelectedValue, Convert.ToInt32(TextBox4.Text), TextBox5.Text);
                if (result == true)
                {
                    Label7.Text = "Saved successfully";
                }
            }
            catch (Exception ex)
            {
                Label7.Text = ex.Message;
            }
        }

        protected void Button2_Click(object sender, EventArgs e)
        {
            Label7.Text = "";
            try
            {
                if (string.IsNullOrWhiteSpace(TextBox2.Text) || string.IsNullOrWhiteSpace(TextBox3.Text))
                {
                    throw new Exception("First name,Last name cannot be empty");
                }

                LINQDatabaseService linqdbs = new LINQDatabaseService();
                Doctor result = linqdbs.GetDoctorInfo(TextBox2.Text, TextBox3.Text);

                TextBox1.Text = result.Medical_Registration_NO_;
                DropDownList1.SelectedValue = result.Health_Profession;
                TextBox4.Text = result.Phone_number.ToString();
                TextBox5.Text = result.Email;
                
            }
            catch (Exception ex)
            {
                TextBox1.Text = "";
                DropDownList1.SelectedValue = "";
                TextBox4.Text = "";
                TextBox5.Text = "";
                Label7.Text = ex.Message;
            }
        }

        protected void DropDownList1_SelectedIndexChanged(object sender, EventArgs e)
        {

        }
    }
}