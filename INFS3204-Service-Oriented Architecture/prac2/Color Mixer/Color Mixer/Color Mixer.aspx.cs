using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace Color_Mixer
{
    public partial class Color_Mixer1 : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        protected void Button1_click(object sender, EventArgs e)
        {

            string mixColor;
            
            ColorMixerRef.Color_Mixer colorMixer = new ColorMixerRef.Color_Mixer();
            ColortoCodConvertorRef.Color_to_Code_Convertor colorConverot = new ColortoCodConvertorRef.Color_to_Code_Convertor();

            mixColor=colorMixer.Mix(colorConverot.Convert(TextBox1.Text), colorConverot.Convert(TextBox2.Text));

            Label2.Text = "Mix = "+mixColor;

        }
    }
}