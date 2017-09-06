using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Text.RegularExpressions;

namespace WebApplication1
{
    public partial class Save_Information : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

       protected void Button1_Click(object sender, EventArgs e)
        {

            string result;
            int n;
            DateTime dt;

            Australian_Postcode_Validation apv = new Australian_Postcode_Validation();
            People_and_Job_Information_Management pjim = new People_and_Job_Information_Management();

            if(TextBox1.Text == "" || TextBox2.Text == "" || TextBox9.Text == "" || TextBox12.Text == "")
            {
                Label15.Text = "Required field validation: firstName, lastName, positionNumber and companyName are required fields.";
            }

            else if (!DateTime.TryParse(TextBox3.Text, out dt))
            {
                Label15.Text = "Birth day format should be: yyyy-MM-dd. ";
            }

            else if (!Regex.IsMatch(TextBox4.Text, @"\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*"))
            {
                Label15.Text = "Email Validation: should be entered in a valid format.";
            }

            else if (!int.TryParse(TextBox9.Text, out n))
            {
                Label15.Text = "Position Number: should be an integer.";
            }

            else if (!int.TryParse(TextBox8.Text, out n))
            {
                Label15.Text = "Postcode: should be an integer.";
            }

            else if (TextBox7.Text == "")
            {
                Label15.Text = "State cannot be empty";
            }

            else if (!apv.PostcodeValidation(Convert.ToInt32(TextBox8.Text), TextBox7.Text))
            {
                Label15.Text = "Postcode Validation: postcode should match state.";
            }

            else
            {
                Person person = new Person();
                Job job = new Job();

                person.firstName = TextBox1.Text;
                person.lastName = TextBox2.Text;
                person.dateOfBirth = Convert.ToDateTime(TextBox3.Text);
                person.email = TextBox4.Text;
                person.streetAddress = TextBox5.Text;
                person.suburb = TextBox6.Text;
                person.state = TextBox7.Text;
                person.postcode = Convert.ToInt32(TextBox8.Text);

                job.positionNumber = Convert.ToInt32(TextBox9.Text);
                job.positionTitle = TextBox10.Text;
                job.positionDescription = TextBox11.Text;
                job.companyName = TextBox12.Text;
                person.job = job;

                result = pjim.SaveInfo(person).ToString();

                if (result == "True")
                {
                    Label15.Text = "Successfully saved";
                }

                else Label15.Text = "Unable to save";
            }

        }

    
    }
}