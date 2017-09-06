<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Search Colleagues.aspx.cs" Inherits="WebApplication1.Search_Colleagues" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
    <style type="text/css">
        #head {
            width:100%; 
            color:#ffffff;
            background-color:#336699;  
            font-family:'Times New Roman';
            width:100%;
            float:left;
            height:auto;
            
        }

        h1 {
            font-size:28px;
            font-weight:bold;
            float:left;
            margin:15px 10px;

        }

        .style {
            font-size:120%;
            font-weight:bolder;
        }

        #navigation {
            background-color:#666699;
            color:#E8E8E8;
            list-style: none;
            float:left;
            width:100%;
            
        }

        ul {
           float:left;
           margin:5px -30px;
        }

        li {
            display:inline;
            float:left;
            border-style:solid;
            border-width:1px;
            border-color:#E8E8E8;
            padding:5px 20px;
            
        }

        #title {
            float:left;
            width:100%;
            height:100%;
            font-size:150%;
            font-weight:bolder;
        }

    </style>

</head>
<body>
    <div>
       <div id="head"><h1><span class="style">M</span>Y <span class="style">ASP.NET A</span>PPLICATION</h1></div>
       <div id="navigation">
           <ul>
               <li><asp:Hyperlink ID="Hyperlink1" runat="server" NavigateUrl="Save Information.aspx">Save Information Page</asp:Hyperlink></li>
               <li><asp:Hyperlink ID="Hyperlink2" runat="server" NavigateUrl="Search Person’s Job.aspx">Search Person’s Job Page</asp:Hyperlink></li>
               <li><asp:Hyperlink ID="Hyperlink3" runat="server" NavigateUrl="Search Colleagues.aspx">Search Colleagues Page</asp:Hyperlink></li>
           </ul>
       </div>
    </div>

    <div id="title">
        <h3>Search Colleagues Page:</h3>
    </div>

    <form id="form1" runat="server">
    <div>
        <asp:Label ID="Label1" runat="server" Text="Search Colleagues"></asp:Label>
        <br />
        <br />
        <table class="auto-style1">
            <tr>
                <td class="auto-style2">
                    <asp:Label ID="Label2" runat="server" Text="Firstname"></asp:Label>
                </td>
                <td class="auto-style3">
                    <asp:Label ID="Label3" runat="server" Text="Lastname"></asp:Label>
                </td>
            </tr>
            <tr>
                <td class="auto-style2">
                    <asp:TextBox ID="TextBox1" runat="server"></asp:TextBox>
                </td>
                <td class="auto-style3">
                    <asp:TextBox ID="TextBox2" runat="server"></asp:TextBox>
                </td>
            </tr>
        </table>
        <br />
    
        <asp:Button ID="Button1" runat="server" Text="Search" OnClick="Button1_Click" />           
        <br />
        <br />
        <asp:Label ID="Label4" runat="server" Text="Colleagues Information:"></asp:Label>
        <br />
        <br />
        <asp:TextBox ID="TextBox3" runat="server" Height="97px" TextMode="MultiLine" Width="684px"></asp:TextBox>
    </div>
    </form>
</body>
</html>
