using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace Buggy_Calculator_Web_Application
{
    public partial class Buggy_Calculator : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            
        }

        protected void Button1_click(object sender, EventArgs e)
        {
            string number1 = TextBox1.Text.Trim();
            string number2 = TextBox2.Text.Trim();
            int num1, num2;

            //reverse number
            if (number1.Length > 0)
            {
                if (number1.Length > 1)
                {
                    string result = "";
                    for (int i = number1.Length - 1; i >= 0; i--)
                    {
                        result = result + number1.Substring(i, 1);
                    }
                    num1 = Convert.ToInt32(result);
                }
                else
                {
                    num1 = Convert.ToInt32(number1);
                }

                if (number2.Length > 0)
                {
                    if (number2.Length > 1)
                    {
                        string result = "";
                        for (int i = number2.Length - 1; i >= 0; i--)
                        {
                            result = result + number2.Substring(i, 1);
                        }
                        num2 = Convert.ToInt32(result);
                    }
                    else
                    {
                        num2 = Convert.ToInt32(number2);
                    }

                    //calculate by select operator
                    string Operator = DropDownList1.SelectedValue;
                    if (Operator == "+")
                    {
                        TextBox3.Text = Convert.ToString(num1 + num2);
                    }
                    if (Operator == "-")
                    {
                        TextBox3.Text = Convert.ToString(num1 - num2);
                    }
                    if (Operator == "*")
                    {
                        TextBox3.Text = Convert.ToString(num1 * num2);
                    }
                    if (Operator == "/")
                    {
                        TextBox3.Text = Convert.ToString(num1 / num2);
                    }
                }
            }


            if (TextBox3.Text != "")
            {
                string number3 = TextBox3.Text.Trim();
                int num3 = Convert.ToInt32(number3);
                TextBox4.Text = Convert.ToString(num3, 2);
            }
        }
    }
}