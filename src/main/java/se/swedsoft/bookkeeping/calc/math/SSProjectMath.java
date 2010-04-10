package se.swedsoft.bookkeeping.calc.math;


import se.swedsoft.bookkeeping.data.SSNewProject;

import java.util.List;


/**
 * User: Andreas Lago
 * Date: 2006-okt-05
 * Time: 15:41:58
 */
public class SSProjectMath {
    private SSProjectMath() {}

    /**
     * Returns one project for the current company.
     *
     * @param iProjects
     * @param iNumber
     * @return The project or null
     */
    public static SSNewProject getProject(List<SSNewProject> iProjects, String iNumber) {
        for (SSNewProject iProject: iProjects) {
            if (iProject.getNumber().equals(iNumber)) {
                return iProject;
            }
        }
        return null;
    }

}
