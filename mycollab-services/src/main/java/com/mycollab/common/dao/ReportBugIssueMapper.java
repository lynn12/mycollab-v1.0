/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.common.dao;

import com.mycollab.common.domain.ReportBugIssueExample;
import com.mycollab.common.domain.ReportBugIssueWithBLOBs;
import com.mycollab.db.persistence.ICrudGenericDAO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

@SuppressWarnings({ "ucd", "rawtypes" })
public interface ReportBugIssueMapper extends ICrudGenericDAO {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_report_bug_issue
     *
     * @mbggenerated Mon Dec 07 11:25:13 ICT 2015
     */
    int countByExample(ReportBugIssueExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_report_bug_issue
     *
     * @mbggenerated Mon Dec 07 11:25:13 ICT 2015
     */
    int deleteByExample(ReportBugIssueExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_report_bug_issue
     *
     * @mbggenerated Mon Dec 07 11:25:13 ICT 2015
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_report_bug_issue
     *
     * @mbggenerated Mon Dec 07 11:25:13 ICT 2015
     */
    int insert(ReportBugIssueWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_report_bug_issue
     *
     * @mbggenerated Mon Dec 07 11:25:13 ICT 2015
     */
    int insertSelective(ReportBugIssueWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_report_bug_issue
     *
     * @mbggenerated Mon Dec 07 11:25:13 ICT 2015
     */
    List<ReportBugIssueWithBLOBs> selectByExampleWithBLOBs(ReportBugIssueExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_report_bug_issue
     *
     * @mbggenerated Mon Dec 07 11:25:13 ICT 2015
     */
    ReportBugIssueWithBLOBs selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_report_bug_issue
     *
     * @mbggenerated Mon Dec 07 11:25:13 ICT 2015
     */
    int updateByExampleSelective(@Param("record") ReportBugIssueWithBLOBs record, @Param("example") ReportBugIssueExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_report_bug_issue
     *
     * @mbggenerated Mon Dec 07 11:25:13 ICT 2015
     */
    int updateByExampleWithBLOBs(@Param("record") ReportBugIssueWithBLOBs record, @Param("example") ReportBugIssueExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_report_bug_issue
     *
     * @mbggenerated Mon Dec 07 11:25:13 ICT 2015
     */
    int updateByPrimaryKeySelective(ReportBugIssueWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_report_bug_issue
     *
     * @mbggenerated Mon Dec 07 11:25:13 ICT 2015
     */
    int updateByPrimaryKeyWithBLOBs(ReportBugIssueWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_report_bug_issue
     *
     * @mbggenerated Mon Dec 07 11:25:13 ICT 2015
     */
    Integer insertAndReturnKey(ReportBugIssueWithBLOBs value);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_report_bug_issue
     *
     * @mbggenerated Mon Dec 07 11:25:13 ICT 2015
     */
    void removeKeysWithSession(List primaryKeys);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_report_bug_issue
     *
     * @mbggenerated Mon Dec 07 11:25:13 ICT 2015
     */
    void massUpdateWithSession(@Param("record") ReportBugIssueWithBLOBs record, @Param("primaryKeys") List primaryKeys);
}