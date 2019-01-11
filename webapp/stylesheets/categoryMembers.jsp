<%@include file="_include.jsp"%>
<e:page stringsVar="msgs" menuItem="departments" locale="#{sessionController.locale}"
	authorized="#{departmentsController.currentUserCanManageDepartmentCategories}">
 <t:htmlTag id="categoryMembers" value="div" styleClass="page-wrapper category-members">
               <t:htmlTag id="header" value="header" styleClass="header">
                    <%@include file="_header.jsp"%>
                </t:htmlTag>
                <t:htmlTag value="div" styleClass="columns">
                    <t:htmlTag value="aside" styleClass="navigation">
                        <%@include file="_navigation.jsp"%>
                    </t:htmlTag>
                    <t:htmlTag value="main" styleClass="content">
                        <t:htmlTag value="div" styleClass="content-inner">

                            <e:form
                                freezeScreenOnSubmit="#{sessionController.freezeScreenOnSubmit}"
                                showSubmitPopupText="#{sessionController.showSubmitPopupText}"
                                showSubmitPopupImage="#{sessionController.showSubmitPopupImage}"
                                id="categoryMembersForm">
                                      <t:htmlTag value="div" styleClass="message">
                                            <e:messages/>
                                      </t:htmlTag>
                                      <t:htmlTag value="div" styleClass="dashboard-header">
                                         <t:htmlTag value="div" styleClass="controlPanel-title">
                                             <t:htmlTag value="h1">
                                                 <t:htmlTag value="span">
                                                     <h:outputText value="#{msgs['CATEGORY_MEMBERS.TITLE']}"/>
                                                 </t:htmlTag>
                                                 <t:htmlTag value="span" styleClass="subtitle title">
                                                    <h:outputText value=" #{departmentsController.categoryToUpdate.label}" escape="false" />
                                                 </t:htmlTag>
                                             </t:htmlTag>
                                         </t:htmlTag>
                                      </t:htmlTag>
                                      <t:htmlTag value="div" styleClass="region" rendered="#{departmentsController.categoryToUpdate.inheritMembers}">
                                          <t:htmlTag value="div" styleClass="form-block">
                                            <e:paragraph value="#{msgs['CATEGORY_MEMBERS.TEXT.INHERIT_DEPARTMENT']}"
                                                    rendered="#{departmentsController.categoryToUpdate.parent == null}" />
                                            <e:paragraph value="#{msgs['CATEGORY_MEMBERS.TEXT.INHERIT_CATEGORY']}"
                                                    rendered="#{departmentsController.categoryToUpdate.parent != null}" />
                                          </t:htmlTag>
                                          <t:htmlTag value="div" styleClass="form-block">
                                                <t:htmlTag value="div" styleClass="form-item display-flex">
                                                    <e:commandButton id="doNotInheritButton" styleClass="button--primary"
                                                        action="#{departmentsController.toggleInheritMembers}"
                                                        value="#{msgs['CATEGORY_MEMBERS.BUTTON.DO_NOT_INHERIT']}" />
                                                    <e:commandButton id="cancelButton_1" action="back" value="#{msgs['CATEGORY_MEMBERS.BUTTON.BACK']}" immediate="true" />
                                                </t:htmlTag>
                                          </t:htmlTag>
                                          <t:htmlTag value="div" styleClass="form-block">
                                            <e:paragraph value="#{msgs['CATEGORY_MEMBERS.TEXT.NO_INHERITED_MEMBER']}"
                                                    rendered="#{empty departmentsController.inheritedMembers}" />

                                            <e:paragraph value="#{msgs['CATEGORY_MEMBERS.TEXT.INHERITED_MEMBERS']}"
                                                    rendered="#{not empty departmentsController.inheritedMembers}" />
                                          </t:htmlTag>
                                          <t:htmlTag value="div" styleClass="form-block">
                                             <t:dataList
                                                     value="#{departmentsController.inheritedMembers}"
                                                     var="departmentManager" >
                                                     <e:li value="#{userFormatter[departmentManager.user]}" />
                                             </t:dataList>
                                          </t:htmlTag>

                                      </t:htmlTag>
                                      <t:htmlTag value="div" styleClass="region" rendered="#{not departmentsController.categoryToUpdate.inheritMembers && departmentsController.categoryToUpdate.parent == null}">
                                        <t:htmlTag value="div" styleClass="form-block">
                                                <e:paragraph value="#{msgs['CATEGORY_MEMBERS.TEXT.NO_INHERIT_DEPARTMENT']}" />
                                        </t:htmlTag>
                                        <t:htmlTag value="div" styleClass="form-block">
                                                <t:htmlTag value="div" styleClass="form-item display-flex">
                                                    <e:commandButton id="inheritDepartmentButton" styleClass="button--primary"
                                                        action="#{departmentsController.toggleInheritMembers}"
                                                        value="#{msgs['CATEGORY_MEMBERS.BUTTON.INHERIT_DEPARTMENT']}" />
                                                    <e:commandButton id="cancelButton_2" action="back" value="#{msgs['CATEGORY_MEMBERS.BUTTON.BACK']}" immediate="true" />
                                                </t:htmlTag>
                                        </t:htmlTag>
                                      </t:htmlTag>
                                      <t:htmlTag value="div" styleClass="region" rendered="#{not departmentsController.categoryToUpdate.inheritMembers && departmentsController.categoryToUpdate.parent != null}">
                                        <t:htmlTag value="div" styleClass="form-block">
                                                <e:paragraph value="#{msgs['CATEGORY_MEMBERS.TEXT.NO_INHERIT_CATEGORY']}" />
                                        </t:htmlTag>
                                        <t:htmlTag value="div" styleClass="form-block">
                                                <t:htmlTag value="div" styleClass="form-item display-flex">
                                                    <e:commandButton id="inheritCategoryButton" styleClass="button--primary"
                                                        action="#{departmentsController.toggleInheritMembers}"
                                                        value="#{msgs['CATEGORY_MEMBERS.BUTTON.INHERIT_CATEGORY']}" />
                                                    <e:commandButton id="cancelButton_3" action="back" value="#{msgs['CATEGORY_MEMBERS.BUTTON.BACK']}" immediate="true" />
                                                </t:htmlTag>
                                        </t:htmlTag>
                                      </t:htmlTag>
                                       <t:htmlTag value="div" styleClass="region" rendered="#{not departmentsController.categoryToUpdate.inheritMembers && empty departmentsController.members}">
                                            <t:htmlTag value="div" styleClass="form-block">
                                                    <e:paragraph value="#{msgs['CATEGORY_MEMBERS.TEXT.NO_MEMBER']}" />
                                            </t:htmlTag>
                                       </t:htmlTag>
                                       <t:htmlTag value="div" styleClass="region" rendered="#{not departmentsController.categoryToUpdate.inheritMembers && not empty departmentsController.members}">
                                            <t:htmlTag value="div" styleClass="form-block">
                                                    <e:paragraph value="#{msgs['CATEGORY_MEMBERS.TEXT.MEMBERS']}" />
                                            </t:htmlTag>
                                            <t:htmlTag value="div" styleClass="form-block">
                                                <e:dataTable
                                					id="memberData" rowIndexVar="variable" styleClass="members_array" value="#{departmentsController.members}"
                                					var="member" border="0" cellspacing="0" cellpadding="0">
                                					<t:column>
                                						<e:text value="#{userFormatter[member.user]}" />
                                					</t:column>
                                					<t:column>
                                                      <t:htmlTag value="div" styleClass="form-item">
                                       		                <e:commandButton id="deleteButton" value="#{msgs['_.BUTTON.DELETE']}"
                                                               action="#{departmentsController.deleteCategoryMember}" >
                                                               <t:updateActionListener value="#{member}" property="#{departmentsController.memberToDelete}" />
                                                            </e:commandButton>
                                                      </t:htmlTag>
                                					</t:column>
                                				</e:dataTable>
                                            </t:htmlTag>
                                      </t:htmlTag>

                                       <t:htmlTag value="div" styleClass="region" rendered="#{not departmentsController.categoryToUpdate.inheritMembers && empty departmentsController.notMembers}">
                                            <t:htmlTag value="div" styleClass="form-block">
                                                    <e:paragraph value="#{msgs['CATEGORY_MEMBERS.TEXT.NO_NOT_MEMBER']}" />
                                            </t:htmlTag>
                                       </t:htmlTag>

                                       <t:htmlTag value="div" styleClass="region" rendered="#{not departmentsController.categoryToUpdate.inheritMembers && not empty departmentsController.notMembers}">
                                            <t:htmlTag value="div" styleClass="form-block">
                                                    <e:paragraph value="#{msgs['CATEGORY_MEMBERS.TEXT.NOT_MEMBERS']}" />
                                            </t:htmlTag>
                                       </t:htmlTag>

                                       <t:htmlTag value="div" styleClass="region" rendered="#{not departmentsController.categoryToUpdate.inheritMembers && not empty departmentsController.notMembers}">
                                            <e:dataTable
                             					id="notMemberData" rowIndexVar="variable" styleClass="members_array"
                             					value="#{departmentsController.notMembers}"
                             					var="departmentManager" border="0" cellspacing="0" cellpadding="0">

                             					<t:column>
                             						<e:text value="#{userFormatter[departmentManager.user]}" />
                             					</t:column>
                             					<t:column>
                             					    <t:htmlTag value="div" styleClass="form-block">
                             					        <t:htmlTag value="div" styleClass="form-item">
                                                            <e:commandButton styleClass="button--secondary" id="addButton" value="#{msgs['CATEGORY_MEMBERS.TEXT.MEMBER_ADD']}"
                                                                action="#{departmentsController.addCategoryMember}" >
                                                                <t:updateActionListener value="#{departmentManager.user}"
                                                                    property="#{departmentsController.memberToAdd}" />
                                                            </e:commandButton>
                             						     </t:htmlTag>
                             						 </t:htmlTag>
                             					</t:column>
                             				</e:dataTable>
                                        </t:htmlTag>
                                        <t:htmlTag value="div" styleClass="form-block" rendered="#{not departmentsController.categoryToUpdate.inheritMembers && not empty departmentsController.notMembers}">
                             					        <t:htmlTag value="div" styleClass="form-item">
                                                            <e:commandButton id="addAllButton" value="#{msgs['CATEGORY_MEMBERS.BUTTON.ADD_ALL']} " styleClass="button--primary"
                                                            	action="#{departmentsController.addAllCategoryMembers}" />
                             						     </t:htmlTag>
                             		    </t:htmlTag>

                            </e:form>
                    </t:htmlTag>
            </t:htmlTag>
            </t:htmlTag>
           <t:htmlTag value="footer" styleClass="footer">
                        <%@include file="_footer.jsp"%>
           </t:htmlTag>
        </t:htmlTag>
</e:page>
