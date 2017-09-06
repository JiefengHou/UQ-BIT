<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Buggy Calculator.aspx.cs" Inherits="Buggy_Calculator_Web_Application.Buggy_Calculator" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
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
        }

        h3 {
            text-transform:uppercase;
            color:#663333;
            font-weight:normal;
            float:left;
            margin-left:10px;
        }

        .table-style {
            float:left;
            width:100%;
            color:#663333;
        }

        td {
            float:left;
            margin-top:10px;
            margin-left:10px;
        }
    </style>
    <title></title>
</head>
<body> 
    <div>
       <div id="head"><h1><span class="style">M</span>Y <span class="style">ASP.NET A</span>PPLICATION</h1></div>
       <div id="navigation">
           <ul>
               <li>Home</li>
               <li>About</li>
           </ul>
       </div>
    </div>
    <div id="title">
        <h3><span class="style">W</span>elcome to <span class="style">B</span>uggy <span class="style">C</span>alculator<span class="style">!</span></h3>
    </div>

    <form id="form1" runat="server">
        <div>

            <table class="table-style">
                <tr>
                    <td>
                        <asp:TextBox ID="TextBox1" runat="server"></asp:TextBox>
                    </td>
                    <td>
                        <asp:DropDownList ID="DropDownList1" runat="server">
                            <asp:ListItem>+</asp:ListItem>
                            <asp:ListItem>-</asp:ListItem>
                            <asp:ListItem>*</asp:ListItem>
                            <asp:ListItem>/</asp:ListItem>
                        </asp:DropDownList>
                    </td>
                    <td>
                        <asp:TextBox ID="TextBox2" runat="server"></asp:TextBox>
                    </td>
                    <td>
                        <asp:Label ID="Label1" runat="server" Text="<span class='style'>=B</span>ASE<span class='style'>10:</span>"></asp:Label>
                    </td>
                    <td>
                        <asp:TextBox ID="TextBox3" runat="server" ReadOnly="true"></asp:TextBox>
                    </td>
                    <td>
                        <asp:Label ID="Label2" runat="server" Text="<span class='style'>=B</span>ASE<span class='style'>2:</span>"></asp:Label>
                    </td>
                    <td>
                        <asp:TextBox ID="TextBox4" runat="server" ReadOnly="true"></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td>
                        <asp:Button ID="Button1" runat="server" Text="Calculate" onclick="Button1_click"/>
                    </td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
            </table>
        </div>
    </form>
</body>
</html>
